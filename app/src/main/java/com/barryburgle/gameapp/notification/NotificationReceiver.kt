package com.barryburgle.gameapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.barryburgle.gameapp.service.notification.NotificationService
import java.time.LocalDateTime

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationService = NotificationService(context)
        val receivedInterval = intent?.getIntExtra(
            AndroidNotificationScheduler.RECURRING_NOTIFICATION_INTERVAL,
            0
        )
        val requestCode =
            intent?.getIntExtra(AndroidNotificationScheduler.REQUEST_CODE, 1) ?: return
        val notificationTitle =
            intent?.getStringExtra(AndroidNotificationScheduler.NOTIFICATION_TITLE) ?: return
        val notificationContent =
            intent?.getStringExtra(AndroidNotificationScheduler.NOTIFICATION_CONTENT) ?: return
        if (receivedInterval!! != 0 && context != null
        ) {
            val notificationScheduler = AndroidNotificationScheduler(context)
            val time = LocalDateTime.now().plusMinutes(receivedInterval.toLong())
            notificationScheduler.schedule(
                requestCode,
                time,
                notificationTitle,
                notificationContent,
                receivedInterval
            )
        }
        notificationService.showNotification(
            notificationTitle,
            notificationContent
        )
    }
}