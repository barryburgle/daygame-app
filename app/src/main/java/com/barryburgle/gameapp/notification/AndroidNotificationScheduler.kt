package com.barryburgle.gameapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneId

class AndroidNotificationScheduler(
    private val context: Context
) : NotificationScheduler {

    companion object {
        const val STICKING_POINTS_REQUEST_CODE = 0
        const val SITTING_REMINDER_REQUEST_CODE = 1
        const val WRITE_HER_AFTER_REQUEST_CODE = 2
        const val REQUEST_CODE: String = "request-code"
        const val RECURRING_NOTIFICATION_INTERVAL: String = "interval"
        const val NOTIFICATION_TITLE: String = "title"
        const val NOTIFICATION_CONTENT: String = "content"
        const val NOTIFICATION_LINK: String = "link"
    }

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(
        requestCode: Int,
        time: LocalDateTime,
        title: String,
        content: String,
        interval: Int?,
        link: String?
    ) {
        val alarmPendingIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(REQUEST_CODE, requestCode)
            putExtra(NOTIFICATION_TITLE, title)
            putExtra(NOTIFICATION_CONTENT, content)
        }
        if (interval != null) {
            alarmPendingIntent.putExtra(RECURRING_NOTIFICATION_INTERVAL, interval)
        }
        if (link != null) {
            alarmPendingIntent.putExtra(NOTIFICATION_LINK, link)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                requestCode,
                alarmPendingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(
        requestCode: Int
    ) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(context, NotificationReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}