package com.barryburgle.gameapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

class AndroidNotificationScheduler(
    private val context: Context
) : NotificationScheduler {

    companion object {
        const val RECURRING_NOTIFICATION_INTERVAL: String = "interval"
        const val NOTIFICATION_TITLE: String = "title"
        const val NOTIFICATION_CONTENT: String = "content"
    }

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(
        time: LocalDateTime,
        title: String,
        content: String,
        interval: Int?
    ) {
        val alarmPendingIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NOTIFICATION_TITLE, title)
            putExtra(NOTIFICATION_CONTENT, content)
        }
        if (interval != null) {
            alarmPendingIntent.putExtra(RECURRING_NOTIFICATION_INTERVAL, interval)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                UUID.randomUUID().hashCode(),
                alarmPendingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}