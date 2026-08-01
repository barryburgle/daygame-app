package com.barryburgle.gameapp.service.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.barryburgle.gameapp.MainActivity
import com.barryburgle.gameapp.R

class NotificationService(
    private val context: Context?
) {

    companion object {
        const val STICKING_POINT_NOTIFICATION_CHANNEL_ID = "sticking_points_reminder"
        const val STICKING_POINT_NOTIFICATION_CHANNEL_NAME = "Sticking points reminder"
        const val LIVE_SESSION_NOTIFICATION_CHANNEL_ID = "live_session_persistent"
        const val LIVE_SESSION_NOTIFICATION_CHANNEL_NAME = "Live Session"
        const val RECORDING_NOTIFICATION_CHANNEL_ID = "recording_status"
        const val RECORDING_NOTIFICATION_CHANNEL_NAME = "Recording"
    }

    private val notificationManager =
        context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(title: String, content: String) {
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        val mainActivityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            mainActivityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val notification =
            NotificationCompat.Builder(context!!, STICKING_POINT_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(mainActivityPendingIntent)
                .build()
        notificationManager.notify(1, notification)
    }
}