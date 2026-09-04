package com.barryburgle.gameapp.model.recording

enum class RecordingStateEnum {
    IDLE,
    RECORDING,
    PLAYING,
    PLAYBACK_PAUSED;

    fun isRecording(): Boolean = this == RECORDING

    fun isPlaying(): Boolean = this == PLAYING || this == PLAYBACK_PAUSED
}

data class RecordingState(
    val state: RecordingStateEnum = RecordingStateEnum.IDLE,
    // the file this state is about - the one being recorded, or the one being played. The enum
    // already says which of the two, so a single field covers both and cannot hold a contradiction
    val activeFileName: String? = null
)

// deliberately kept out of RecordingState: this ticks a few times a second, and RecordingState is
// merged into InputState, so putting it there would recompose the whole Game tab at that rate
data class PlaybackProgress(
    val positionMs: Int = 0,
    val durationMs: Int = 0 // 0 when nothing is loaded: the bar renders disabled
)
