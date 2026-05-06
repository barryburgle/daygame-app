package com.barryburgle.gameapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.barryburgle.gameapp.service.notification.NotificationService

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationService = NotificationService(context)
        val notificationTitle =
            intent?.getStringExtra(AndroidNotificationScheduler.NOTIFICATION_TITLE) ?: return
        val notificationContent =
            intent?.getStringExtra(AndroidNotificationScheduler.NOTIFICATION_CONTENT) ?: return
        notificationService.showNotification(
            notificationTitle,
            notificationContent
        )
    }
}