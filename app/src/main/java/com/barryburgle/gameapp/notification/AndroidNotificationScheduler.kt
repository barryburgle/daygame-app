package com.barryburgle.gameapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.barryburgle.gameapp.notification.state.NotificationState
import java.time.ZoneId

class AndroidNotificationScheduler(
    private val context: Context
) : NotificationScheduler {

    companion object{
        const val STICKING_POINTS_ID: String = "stiking-points"
        const val DATE_ID: String = "date"
    }

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: NotificationState) {
        val alarmPendingIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(STICKING_POINTS_ID, item.lastSessionStickingPoints)
            putExtra(DATE_ID, item.lastSessionDate)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.notificationTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                alarmPendingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(item: NotificationState) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, NotificationReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}