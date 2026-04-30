package com.barryburgle.gameapp.service.notification

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.barryburgle.gameapp.MainActivity
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.database.GameAppDatabase
import com.barryburgle.gameapp.service.batch.BatchSessionService
import com.barryburgle.gameapp.service.notification.PersistentNotificationService.Companion.LIVE_SESSIONS_START_HOUR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersistentNotificationService : Service() {

    companion object {
        const val LIVE_SESSIONS_START_HOUR = "LIVE SESSIONS START HOUR"
        const val ACTION_NEW_SET = "ACTION_NEW_SET"
        const val ACTION_NEW_CONVERSATION = "ACTION_NEW_CONVERSATION"
        const val ACTION_NEW_CONTACT = "ACTION_NEW_CONTACT"
    }

    private var startHour: String? = null

    private fun setStartHour(intent: Intent?) {
        val hour = intent?.getStringExtra(LIVE_SESSIONS_START_HOUR)
        if (hour != null) {
            startHour = hour
        }
    }

    private fun handleNewSetAction(intent: Intent?, abstractSessionDao: AbstractSessionDao) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val batchSessionService = BatchSessionService()
                val lastSession = abstractSessionDao.getLastSession().first()
                lastSession.let { session ->
                    val updatedSession = batchSessionService.init(
                        session.id.toString(),
                        session.date.substring(0, 10),
                        session.startHour.substring(11, 16),
                        session.endHour.substring(11, 16),
                        (session.sets + 1).toString(),
                        session.convos
                            .toString(),
                        session.contacts.toString(),
                        session.stickingPoints
                    )
                    abstractSessionDao.insert(updatedSession)
                    withContext(Dispatchers.Main) {
                        updateNotification()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // TODO: use follow count
    private fun handleNewConversationAction(
        intent: Intent?,
        abstractSessionDao: AbstractSessionDao
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val batchSessionService = BatchSessionService()
                val lastSession = abstractSessionDao.getLastSession().first()
                lastSession.let { session ->
                    val updatedSession = batchSessionService.init(
                        session.id.toString(),
                        session.date.substring(0, 10),
                        session.startHour.substring(11, 16),
                        session.endHour.substring(11, 16),
                        session.sets.toString(),
                        (session.convos + 1).toString(),
                        session.contacts.toString(),
                        session.stickingPoints
                    )
                    abstractSessionDao.insert(updatedSession)
                    withContext(Dispatchers.Main) {
                        updateNotification()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // TODO: use follow count
    private fun handleNewContactAction(
        intent: Intent?,
        abstractSessionDao: AbstractSessionDao
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val batchSessionService = BatchSessionService()
                val lastSession = abstractSessionDao.getLastSession().first()
                lastSession.let { session ->
                    val updatedSession = batchSessionService.init(
                        session.id.toString(),
                        session.date.substring(0, 10),
                        session.startHour.substring(11, 16),
                        session.endHour.substring(11, 16),
                        session.sets.toString(),
                        session.convos
                            .toString(),
                        (session.contacts + 1).toString(),
                        session.stickingPoints
                    )
                    abstractSessionDao.insert(updatedSession)
                    withContext(Dispatchers.Main) {
                        updateNotification()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setStartHour(intent)
        val database = GameAppDatabase.getInstance(applicationContext)
        val abstractSessionDao = database!!.abstractSessionDao
        when (intent?.action) {
            ACTION_NEW_SET -> {
                handleNewSetAction(intent, abstractSessionDao)
                return START_STICKY
            }

            ACTION_NEW_CONVERSATION -> {
                handleNewConversationAction(intent, abstractSessionDao)
                return START_STICKY
            }

            ACTION_NEW_CONTACT -> {
                handleNewContactAction(intent, abstractSessionDao)
                return START_STICKY
            }
        }
        return updateNotification()
    }

    fun updateNotification(): Int {
        val newSetPendingIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, PersistentNotificationService::class.java).apply {
                action = ACTION_NEW_SET
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val newConversationPendingIntent = PendingIntent.getService(
            this,
            1,
            Intent(this, PersistentNotificationService::class.java).apply {
                action = ACTION_NEW_CONVERSATION
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val newContactPendingIntent = PendingIntent.getService(
            this,
            2,
            Intent(this, PersistentNotificationService::class.java).apply {
                action = ACTION_NEW_CONTACT
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val tapPendingIntent = PendingIntent.getActivity(
            this,
            3,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // TODO: how to make the notification unswipeable until I send the "disappear notification" message on channel?
        val notification = NotificationCompat.Builder(
            this,
            NotificationService.LIVE_SESSION_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Live session")
            .setContentText(if (startHour != null) "Started at " + startHour else "")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(tapPendingIntent)
            .addAction(0, "New set", newSetPendingIntent)
            .addAction(0, "New conversation", newConversationPendingIntent)
            .addAction(0, "New contact", newContactPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(100, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(100, notification)
        }

        return START_STICKY
    }
}