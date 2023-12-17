package com.barryburgle.gameapp.service.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.barryburgle.gameapp.MainActivity
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.notification.state.NotificationState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NotificationService(
    private val context: Context?
) {

    companion object {
        const val STICKING_POINT_NOTIFICATION_CHANNEL_ID = "daygame_channel_id"
        const val STICKING_POINT_NOTIFICATION_CHANNEL_NAME = "Daygame"

        fun createNotificationState(
            notificationDate: String,
            lastSessionDate: String,
            lastSessionStickingPoints: String
        ): NotificationState {
            return NotificationState(
                getNotificationLocalDateTime(notificationDate.substring(0, 5)),
                lastSessionDate,
                lastSessionStickingPoints
            )
        }

        fun getNotificationLocalDateTime(hour: String): LocalDateTime {
            val time = LocalTime.parse(hour, DateTimeFormatter.ofPattern("HH:mm"))
            val tomorrowDate = LocalDate.now().plusDays(1)
            return LocalDateTime.of(tomorrowDate, time)
        }
    }

    private val notificationManager =
        context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(lastSessionDate: String, lastSessionStickingPoints: String) {
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
                .setContentTitle("Sticking points from ${lastSessionDate} session")
                .setContentText(lastSessionStickingPoints)
                .setContentIntent(mainActivityPendingIntent)
                .build()
        notificationManager.notify(1, notification)
    }
}