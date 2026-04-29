package com.barryburgle.gameapp.service.notification

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.database.GameAppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PersistentNotificationService : Service() {

    companion object {
        const val LIVE_SESSIONS_START_HOUR = "LIVE SESSIONS START HOUR"
        const val ACTION_NEW_SET = "ACTION_NEW_SET"
        const val ACTION_NEW_CONVERSATION = "ACTION_NEW_CONVERSATION"
        const val ACTION_NEW_CONTACT = "ACTION_NEW_CONTACT"
    }

    private fun handleNewSetAction(intent: Intent?, abstractSessionDao: AbstractSessionDao): Int {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lastSession = abstractSessionDao.getLastSession().first()
                lastSession.let { session ->
                    val updatedSets = session.sets + 1
                    val updatedSession = session.copy(sets = updatedSets)
                    abstractSessionDao.insert(updatedSession)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return updateNotification(intent)
    }

    private fun handleNewConversationAction(
        intent: Intent?,
        abstractSessionDao: AbstractSessionDao
    ): Int {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lastSession = abstractSessionDao.getLastSession().first()
                lastSession.let { session ->
                    val updatedConversations = session.conversations + 1
                    val updatedSession = session.copy(conversations = updatedConversations)
                    abstractSessionDao.insert(updatedSession)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return updateNotification(intent)
    }

    private fun handleNewContactAction(
        intent: Intent?,
        abstractSessionDao: AbstractSessionDao
    ): Int {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lastSession = abstractSessionDao.getLastSession().first()
                lastSession.let { session ->
                    val updatedContacts = session.contacts + 1
                    val updatedSession = session.copy(contacts = updatedContacts)
                    abstractSessionDao.insert(updatedSession)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return updateNotification(intent)
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val database = GameAppDatabase.getInstance(applicationContext)
        val abstractSessionDao = database!!.abstractSessionDao
        when (intent?.action) {
            ACTION_NEW_SET -> {
                return handleNewSetAction(intent, abstractSessionDao)
            }

            ACTION_NEW_CONVERSATION -> {
                return START_STICKY
            }

            ACTION_NEW_CONTACT -> {
                return START_STICKY
            }
        }
        return updateNotification(intent)
    }

    fun updateNotification(intent: Intent?): Int {
        val parsedStartHour = "Started at " + intent?.getStringExtra(LIVE_SESSIONS_START_HOUR)
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
        val notification = NotificationCompat.Builder(
            this,
            NotificationService.LIVE_SESSION_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Live session")
            .setContentText("${parsedStartHour} [ + recap + tap to add lead]")
            .setOngoing(true)
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