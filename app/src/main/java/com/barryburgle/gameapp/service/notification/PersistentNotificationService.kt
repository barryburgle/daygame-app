package com.barryburgle.gameapp.service.notification

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.barryburgle.gameapp.R

class PersistentNotificationService : Service() {

    companion object {
        const val ACTION_STOP = "com.barryburgle.gameapp.STOP_SESSION"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val eventName = intent?.getStringExtra("EVENT_NAME") ?: "Active Event"
        val stopIntent = Intent(this, PersistentNotificationService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(
            this,
            NotificationService.LIVE_SESSION_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Live session")
            .setContentText("[Start time here + recap] + tap to add lead")
            .setOngoing(true)
            .addAction(0, "New set", stopPendingIntent)
            .addAction(0, "New conversation", stopPendingIntent)
            .addAction(0, "New contact", stopPendingIntent)
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