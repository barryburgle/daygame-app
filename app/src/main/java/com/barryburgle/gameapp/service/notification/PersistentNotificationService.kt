package com.barryburgle.gameapp.service.notification

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.barryburgle.gameapp.MainActivity
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.dao.pinpoint.PinPointDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.database.GameAppDatabase
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.pinpoint.PinPointTypeEnum
import com.barryburgle.gameapp.model.session.PinPoint
import com.barryburgle.gameapp.service.EntityService
import com.barryburgle.gameapp.service.batch.BatchSessionService
import com.barryburgle.gameapp.ui.utilities.dialog.passInitialValue
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import kotlin.coroutines.resume

class PersistentNotificationService : Service() {

    companion object {
        const val LIVE_SESSIONS_START_HOUR = "LIVE SESSIONS START HOUR"
        const val IS_FOLLOW_COUNT_ACTIVE = "IS FOLLOW COUNT ACTIVE"
        const val ACTION_NEW_SET = "ACTION_NEW_SET"
        const val ACTION_NEW_CONVERSATION = "ACTION_NEW_CONVERSATION"
        const val ACTION_NEW_CONTACT = "ACTION_NEW_CONTACT"
    }

    private var startHour: String? = null
    private var isFollowCountActive: Boolean = false
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private fun updateServiceState(intent: Intent?) {
        if (intent?.hasExtra(LIVE_SESSIONS_START_HOUR) == true) {
            startHour = intent.getStringExtra(LIVE_SESSIONS_START_HOUR)
        }
        if (intent?.hasExtra(IS_FOLLOW_COUNT_ACTIVE) == true) {
            isFollowCountActive = intent.getBooleanExtra(IS_FOLLOW_COUNT_ACTIVE, false)
        }
    }

    private fun handleNewSetAction(
        abstractSessionDao: AbstractSessionDao,
        pinPointDao: PinPointDao,
        settingDao: SettingDao
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val batchSessionService = BatchSessionService()
                val liveSession = abstractSessionDao.getLastLiveSession().firstOrNull()
                val updatedSession = if (liveSession != null) {
                    batchSessionService.init(
                        liveSession.id.toString(),
                        liveSession.date.substring(0, 10),
                        liveSession.startHour.substring(11, 16),
                        liveSession.endHour.substring(11, 16),
                        (liveSession.sets + 1).toString(),
                        liveSession.convos.toString(),
                        liveSession.contacts.toString(),
                        liveSession.stickingPoints
                    )
                } else {
                    val dateTime = passInitialValue(true, null, "")
                    batchSessionService.init(
                        null,
                        dateTime.substring(0, 10),
                        startHour!!,
                        startHour!!,
                        "1",
                        "0",
                        "0",
                        ""
                    )
                }
                val sessionId = abstractSessionDao.insert(updatedSession)
                val location = getLocation()
                if (location != null) {
                    if (settingDao.getPinPointInteractions()
                            .firstOrNull()
                            ?.toBoolean() ?: true
                    ) {
                        val pinPointLocalDateTime = LocalDateTime.now()
                        pinPointDao.insert(
                            PinPoint(
                                id = null,
                                sourceEventId = sessionId,
                                sourceEventType = EventTypeEnum.SESSION.getField().lowercase(),
                                pinPointType = PinPointTypeEnum.SET.getField(),
                                localTimestamp = pinPointLocalDateTime.toString()
                                    .substring(0, 19) + "Z",
                                longitude = location?.longitude ?: 0.0,
                                latitude = location?.latitude ?: 0.0,
                                dayOfWeek = EntityService.getDayOfWeek(pinPointLocalDateTime).value
                            )
                        )
                    }
                }
                withContext(Dispatchers.Main) {
                    updateNotification(
                        updatedSession.sets,
                        updatedSession.convos,
                        updatedSession.contacts
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleNewConversationAction(
        abstractSessionDao: AbstractSessionDao, pinPointDao: PinPointDao,
        settingDao: SettingDao
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val batchSessionService = BatchSessionService()
                val liveSession = abstractSessionDao.getLastLiveSession().firstOrNull()
                val updatedSession = if (liveSession != null) {
                    batchSessionService.init(
                        liveSession.id.toString(),
                        liveSession.date.substring(0, 10),
                        liveSession.startHour.substring(11, 16),
                        liveSession.endHour.substring(11, 16),
                        if (isFollowCountActive) (liveSession.sets + 1).toString() else liveSession.sets.toString(),
                        (liveSession.convos + 1).toString(),
                        liveSession.contacts.toString(),
                        liveSession.stickingPoints
                    )
                } else {
                    val dateTime = passInitialValue(true, null, "")
                    batchSessionService.init(
                        null,
                        dateTime.substring(0, 10),
                        startHour!!,
                        startHour!!,
                        if (isFollowCountActive) "1" else "0",
                        "1",
                        "0",
                        ""
                    )
                }
                val sessionId = abstractSessionDao.insert(updatedSession)
                val location = getLocation()
                if (location != null) {
                    if (settingDao.getPinPointInteractions()
                            .firstOrNull()
                            ?.toBoolean() ?: true
                    ) {
                        val pinPointLocalDateTime = LocalDateTime.now()
                        pinPointDao.insert(
                            PinPoint(
                                id = null,
                                sourceEventId = sessionId,
                                sourceEventType = EventTypeEnum.SESSION.getField().lowercase(),
                                pinPointType = PinPointTypeEnum.CONVERSATION.getField(),
                                localTimestamp = pinPointLocalDateTime.toString()
                                    .substring(0, 19) + "Z",
                                longitude = location?.longitude ?: 0.0,
                                latitude = location?.latitude ?: 0.0,
                                dayOfWeek = EntityService.getDayOfWeek(pinPointLocalDateTime).value
                            )
                        )
                    }
                }
                withContext(Dispatchers.Main) {
                    updateNotification(
                        updatedSession.sets,
                        updatedSession.convos,
                        updatedSession.contacts
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleNewContactAction(
        abstractSessionDao: AbstractSessionDao, pinPointDao: PinPointDao,
        settingDao: SettingDao
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val batchSessionService = BatchSessionService()
                val liveSession = abstractSessionDao.getLastLiveSession().firstOrNull()
                val updatedSession = if (liveSession != null) {
                    batchSessionService.init(
                        liveSession.id.toString(),
                        liveSession.date.substring(0, 10),
                        liveSession.startHour.substring(11, 16),
                        liveSession.endHour.substring(11, 16),
                        if (isFollowCountActive) (liveSession.sets + 1).toString() else liveSession.sets.toString(),
                        if (isFollowCountActive) (liveSession.convos + 1).toString() else liveSession.convos.toString(),
                        (liveSession.contacts + 1).toString(),
                        liveSession.stickingPoints
                    )
                } else {
                    val dateTime = passInitialValue(true, null, "")
                    batchSessionService.init(
                        null,
                        dateTime.substring(0, 10),
                        startHour!!,
                        startHour!!,
                        if (isFollowCountActive) "1" else "0",
                        if (isFollowCountActive) "1" else "0",
                        "1",
                        ""
                    )
                }
                val sessionId = abstractSessionDao.insert(updatedSession)
                val location = getLocation()
                if (location != null) {
                    if (settingDao.getPinPointInteractions()
                            .firstOrNull()
                            ?.toBoolean() ?: true
                    ) {
                        val pinPointLocalDateTime = LocalDateTime.now()
                        pinPointDao.insert(
                            PinPoint(
                                id = null,
                                sourceEventId = sessionId,
                                sourceEventType = EventTypeEnum.SESSION.getField().lowercase(),
                                pinPointType = PinPointTypeEnum.CONTACT.getField(),
                                localTimestamp = pinPointLocalDateTime.toString()
                                    .substring(0, 19) + "Z",
                                longitude = location?.longitude ?: 0.0,
                                latitude = location?.latitude ?: 0.0,
                                dayOfWeek = EntityService.getDayOfWeek(pinPointLocalDateTime).value
                            )
                        )
                    }
                    withContext(Dispatchers.Main) {
                        updateNotification(
                            updatedSession.sets,
                            updatedSession.convos,
                            updatedSession.contacts
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun getLocation(): Location? {
        val hasFine = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            return null
        }
        val cachedLocation = suspendCancellableCoroutine<Location?> { continuation ->
            val locationRequest = CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setDurationMillis(5000)
                .build()
            fusedLocationClient.getCurrentLocation(locationRequest, null)
                .addOnSuccessListener { location ->
                    if (continuation.isActive) continuation.resume(location)
                }
                .addOnFailureListener {
                    if (continuation.isActive) continuation.resume(null)
                }
        }
        if (cachedLocation != null) {
            return cachedLocation
        }
        return suspendCancellableCoroutine { continuation ->
            val cts = CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cts.token
            )
                .addOnSuccessListener { location ->
                    if (continuation.isActive) continuation.resume(location)
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    if (continuation.isActive) continuation.resume(null)
                }
            continuation.invokeOnCancellation {
                cts.cancel()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateServiceState(intent)
        val database = GameAppDatabase.getInstance(applicationContext)
        val abstractSessionDao = database!!.abstractSessionDao
        val pinPointDao = database!!.pinPointDao
        val settingDao = database!!.settingDao
        when (intent?.action) {
            ACTION_NEW_SET -> {
                handleNewSetAction(abstractSessionDao, pinPointDao, settingDao)
                return START_STICKY
            }

            ACTION_NEW_CONVERSATION -> {
                handleNewConversationAction(abstractSessionDao, pinPointDao, settingDao)
                return START_STICKY
            }

            ACTION_NEW_CONTACT -> {
                handleNewContactAction(abstractSessionDao, pinPointDao, settingDao)
                return START_STICKY
            }
        }
        return updateNotification(0, 0, 0)
    }

    fun updateNotification(sets: Int, conversations: Int, contacts: Int): Int {
        val newSetPendingIntent = PendingIntent.getService(
            this, 0, Intent(this, PersistentNotificationService::class.java).apply {
                action = ACTION_NEW_SET
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val newConversationPendingIntent = PendingIntent.getService(
            this, 1, Intent(this, PersistentNotificationService::class.java).apply {
                action = ACTION_NEW_CONVERSATION
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val newContactPendingIntent = PendingIntent.getService(
            this, 2, Intent(this, PersistentNotificationService::class.java).apply {
                action = ACTION_NEW_CONTACT
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val tapPendingIntent = PendingIntent.getActivity(
            this, 3, Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        var contentText = ""
        if (sets > 0) {
            contentText += "$sets set"
            if (sets > 1) {
                contentText += "s"
            }
        }
        if (conversations > 0) {
            contentText += ", $conversations conversation"
            if (conversations > 1) {
                contentText += "s"
            }
        }
        if (contacts > 0) {
            contentText += ", $contacts contact"
            if (contacts > 1) {
                contentText += "s"
            }
        }
        val notification = NotificationCompat.Builder(
            this, NotificationService.LIVE_SESSION_NOTIFICATION_CHANNEL_ID
        ).setSmallIcon(R.drawable.notification)
            .setContentTitle("Session started at " + startHour)
            .setContentText(contentText)
            .setOngoing(true).setOnlyAlertOnce(true).setContentIntent(tapPendingIntent)
            .addAction(R.drawable.set_action, "New set", newSetPendingIntent)
            .addAction(
                R.drawable.conversation_action,
                "New conversation",
                newConversationPendingIntent
            )
            .addAction(R.drawable.contact_action, "New contact", newContactPendingIntent).setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            ).setPriority(NotificationCompat.PRIORITY_LOW).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(100, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(100, notification)
        }

        return START_STICKY
    }
}