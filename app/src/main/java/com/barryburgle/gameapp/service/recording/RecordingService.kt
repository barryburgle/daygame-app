package com.barryburgle.gameapp.service.recording

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.model.recording.PlaybackProgress
import com.barryburgle.gameapp.model.recording.RecordingState
import com.barryburgle.gameapp.model.recording.RecordingStateEnum
import com.barryburgle.gameapp.service.notification.NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.Date

class RecordingService : Service() {

    companion object {
        const val ACTION_START_RECORDING = "ACTION_START_RECORDING"
        const val ACTION_STOP_RECORDING = "ACTION_STOP_RECORDING"
        const val ACTION_DISCARD_RECORDING = "ACTION_DISCARD_RECORDING"
        const val ACTION_START_PLAYBACK = "ACTION_START_PLAYBACK"
        const val ACTION_PAUSE_PLAYBACK = "ACTION_PAUSE_PLAYBACK"
        const val ACTION_STOP_PLAYBACK = "ACTION_STOP_PLAYBACK"
        const val ACTION_SEEK_PLAYBACK = "ACTION_SEEK_PLAYBACK"
        const val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        const val EXTRA_FILE_NAME = "EXTRA_FILE_NAME"
        const val EXTRA_POSITION_MS = "EXTRA_POSITION_MS"
        const val EXTRA_FOLDER = "EXTRA_FOLDER"
        const val PROGRESS_TICK_MS = 250L
        const val RECORDING_FILE_NAME = "dg"
        const val RECORDING_FILE_EXTENSION = ".m4a"
        const val NOTIFICATION_ID = 101

        private val _state = MutableStateFlow(RecordingState())
        val state = _state.asStateFlow() // non-mutable public state flow

        // deliberately not part of RecordingState: this ticks ~4x/second and RecordingState is
        // merged into InputState, which would recompose the whole Game tab at that rate
        private val _playbackProgress = MutableStateFlow(PlaybackProgress())
        val playbackProgress = _playbackProgress.asStateFlow()

        // the recordings list is re-scanned on every recorder state change: this is the second
        // trigger, for when the folder changes without a state transition (i.e. a delete)
        // check _recordings in InputViewModel
        private val _recordingsVersion = MutableStateFlow(0)
        val recordingsVersion = _recordingsVersion.asStateFlow()

        // delete a recording with fileName in folder
        // call from a background dispatcher: it touches the filesystem (viewModelScope.launch(Dispatchers.IO))
        fun deleteRecording(folder: String, fileName: String) {
            File(recordingsFolder(folder), fileName).delete()
            _recordingsVersion.update { it + 1 } // trigger _recordings update InputViewModel
        }

        // delete recordings that belong to a sessionId
        // also call from a background dispatcher
        fun deleteRecordingsOf(folder: String, sessionId: Long) {
            val owned = recordingsOf(sessionId, listRecordings(folder))
            owned.forEach { fileName -> File(recordingsFolder(folder), fileName).delete() }
            if (owned.isNotEmpty()) _recordingsVersion.update { it + 1 }
        }

        // delete all recording files in the recordings folder
        // also call from a background dispatcher
        fun deleteAllRecordings(folder: String) {
            val owned = listRecordings(folder).filter { isAppRecording(it) }
            owned.forEach { fileName -> File(recordingsFolder(folder), fileName).delete() }
            if (owned.isNotEmpty()) _recordingsVersion.update { it + 1 }
        }

        // TODO: this only works because the app holds MANAGE_EXTERNAL_STORAGE - access to the whole
        //  device. Instead, the user should pick one folder and the app gets a persistable
        //  content:// tree for it and nothing else.
        //  That is an app-wide change (CSV export and backup have the same dependency)
        fun recordingsFolder(folder: String): File = File(
            Environment.getExternalStorageDirectory(),
            folder.ifBlank { SettingDao.DEFAULT_RECORDINGS_FOLDER }
        )

        // lists every recording of every session: session cards filter by their own dg<id>_ prefix.
        // descending so each card lists newest first
        fun listRecordings(folder: String): List<String> = recordingsFolder(folder)
            .listFiles()?.filter { it.name.endsWith(RECORDING_FILE_EXTENSION) }
            ?.map { it.name } ?.sortedDescending() ?: emptyList()


        // return list of recordings for a session
        fun recordingsOf(sessionId: Long?, recordings: List<String>): List<String> = recordings.filter {
            // the trailing underscore is what keeps session 1 from matching session 11 files
            it.startsWith("$RECORDING_FILE_NAME${sessionId}_")
        }

        // get the duration of a recording file
        // MediaPlayer only knows the duration of the file it has loaded. This function is used to
        // get the duration of a file that isn't playing.
        // it opens the file, so call it from a background dispatcher
        fun durationOf(folder: String, fileName: String): Int {
            val retriever = MediaMetadataRetriever()
            return try {
                retriever.setDataSource(File(recordingsFolder(folder), fileName).absolutePath)
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?.toIntOrNull() ?: 0
            } catch (e: Exception) {
                Log.e("RecordingService", e.message, e)
                0
            } finally {
                retriever.release()
            }
        }

        // move recordings from a folder to other
        // call from a background dispatcher
        fun moveRecordings(fromFolder: String, toFolder: String): Int {
            val source = recordingsFolder(fromFolder)
            val destination = recordingsFolder(toFolder)
            if (source.absolutePath == destination.absolutePath) {
                return 0
            }
            if (!destination.exists()) {
                destination.mkdirs()
            }
            var moved = 0
            listRecordings(fromFolder).filter { isAppRecording(it) }.forEach { fileName ->
                try {
                    Files.move(
                        File(source, fileName).toPath(),
                        File(destination, fileName).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                    moved++
                } catch (e: Exception) {
                    Log.e("RecordingService", e.message, e)
                }
            }
            _recordingsVersion.update { it + 1 }
            return moved
        }

        // check if file was generated by this app (contains dg<id>_ prefix)
        fun isAppRecording(fileName: String): Boolean =
            Regex("^$RECORDING_FILE_NAME\\d+_.*\\$RECORDING_FILE_EXTENSION$").matches(fileName)
    }

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var progressJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_RECORDING -> startRecording(
                intent.getLongExtra(EXTRA_SESSION_ID, 0),
                intent.getStringExtra(EXTRA_FOLDER).orEmpty()
            )

            ACTION_STOP_RECORDING -> stopRecording()
            ACTION_DISCARD_RECORDING -> discardRecording(
                intent.getStringExtra(EXTRA_FOLDER).orEmpty()
            )
            ACTION_START_PLAYBACK -> startPlayback(
                intent.getStringExtra(EXTRA_FILE_NAME),
                intent.getStringExtra(EXTRA_FOLDER).orEmpty()
            )

            ACTION_PAUSE_PLAYBACK -> pausePlayback()
            ACTION_STOP_PLAYBACK -> stopPlayback()
            ACTION_SEEK_PLAYBACK -> seekPlayback(intent.getIntExtra(EXTRA_POSITION_MS, 0))
        }
        return START_NOT_STICKY
    }

    private fun startRecording(sessionId: Long, folder: String) {
        // if already recording or paused, skip
        if (_state.value.state.isRecording()) {
            return
        }
        // the mic must never start capturing over playing audio (not stopPlayback(): this service
        // is about to record, it must not stop itself)
        if (_state.value.state.isPlaying()) {
            releasePlayer()
            _state.value = RecordingState()
        }
        // setup notification
        val notification = NotificationCompat.Builder(
            this, NotificationService.RECORDING_NOTIFICATION_CHANNEL_ID
        ).setSmallIcon(R.drawable.notification)
            .setContentTitle("Recording in progress")
            .setOngoing(true).setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW).build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        // create recordings directory if it doesn't exist yet and set audio config
        try {
            val recordingDir = recordingsFolder(folder)
            if (!recordingDir.exists()) {
                recordingDir.mkdirs()
            }
            val outputFile = File(
                recordingDir,
                RECORDING_FILE_NAME + sessionId + SimpleDateFormat("_yyyyMMdd_HHmmss'$RECORDING_FILE_EXTENSION'").format(
                    Date()
                )
            )
            recorder = (
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    MediaRecorder(this)
                else
                    // TODO: will need to update this to the new constructor
                    // deprecated, but only option that exists on API 27–30 devices, our minSdk
                    // is 27 at the moment
                    @Suppress("DEPRECATION") MediaRecorder()).apply {
                        setAudioSource(MediaRecorder.AudioSource.MIC)
                        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                        setAudioEncodingBitRate(64_000) // quite low bit rate, but enough for speech
                        setAudioSamplingRate(44_100)
                        setOutputFile(outputFile.absolutePath)
                        prepare()
                        start()
                    }
            _state.value = RecordingState(RecordingStateEnum.RECORDING, outputFile.name)
        } catch (e: Exception) {
            Log.e("RecordingService", e.message, e)
            recorder?.release()
            recorder = null
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelfIfIdle()
        }
    }

    private fun stopRecording() {
        try {
            recorder?.stop()
        } catch (e: RuntimeException) {
            // stop() right after start() captured no valid data
            Log.e("RecordingService", e.message, e)
        }
        recorder?.release()
        recorder = null
        if (_state.value.state.isRecording()) {
            _state.value = RecordingState()
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelfIfIdle()
    }

    private fun discardRecording(folder: String) {
        val currentFileName = _state.value.activeFileName
        stopRecording()
        if (currentFileName != null) {
            File(recordingsFolder(folder), currentFileName).delete()
        } else {
            throw RuntimeException("Not active file to discard")
        }
    }

    private fun startPlayback(fileName: String?, folder: String) {
        // no playing back while the mic is capturing
        if (fileName == null || _state.value.state.isRecording()) {
            return
        }
        // same file paused: pick up where it was left
        if (_state.value.state == RecordingStateEnum.PLAYBACK_PAUSED && _state.value.activeFileName == fileName) {
            player?.start()
            _state.value = RecordingState(RecordingStateEnum.PLAYING, fileName)
            startProgressTicker()
            return
        }
        releasePlayer()
        try {
            player = MediaPlayer().apply {
                setDataSource(File(recordingsFolder(folder), fileName).absolutePath)
                setOnCompletionListener { stopPlayback() }
                prepare()
                start()
            }
            _state.value = RecordingState(RecordingStateEnum.PLAYING, fileName)
            startProgressTicker()
        } catch (e: Exception) {
            Log.e("RecordingService", e.message, e)
            releasePlayer()
            _state.value = RecordingState()
            stopSelfIfIdle()
        }
    }

    private fun pausePlayback() {
        if (_state.value.state == RecordingStateEnum.PLAYING) {
            player?.pause()
            _state.value = RecordingState(
                RecordingStateEnum.PLAYBACK_PAUSED,
                _state.value.activeFileName
            )
            // stop ticking but publish where it stopped: the thumb stays there and stays draggable
            progressJob?.cancel()
            publishProgress()
        }
    }

    private fun seekPlayback(positionMs: Int) {
        if (_state.value.state.isPlaying()) {
            player?.seekTo(positionMs)
            // publish right away so the thumb doesn't snap back before the next tick
            publishProgress()
        }
    }

    private fun stopPlayback() {
        releasePlayer()
        if (_state.value.state.isPlaying()) {
            _state.value = RecordingState()
        }
        stopSelfIfIdle()
    }

    private fun startProgressTicker() {
        progressJob?.cancel()
        progressJob = serviceScope.launch {
            while (isActive) {
                publishProgress()
                delay(PROGRESS_TICK_MS)
            }
        }
    }

    private fun publishProgress() {
        player?.let { _playbackProgress.value = PlaybackProgress(it.currentPosition, it.duration) }
    }

    private fun releasePlayer() {
        progressJob?.cancel()
        _playbackProgress.value = PlaybackProgress()
        player?.release()
        player = null
    }

    // recorder and player share this service: stopping one must not tear down the other
    private fun stopSelfIfIdle() {
        if (recorder == null && player == null) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        if (_state.value.state.isRecording()) {
            stopRecording()
        }
        releasePlayer()
        _state.value = RecordingState()
        serviceScope.cancel()
        super.onDestroy()
    }
}
