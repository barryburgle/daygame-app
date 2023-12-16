package com.barryburgle.gameapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.service.notification.NotificationService

class NotificationReceiver : BroadcastReceiver() {

    // TODO: integrate daggerhilt for injection of dependecies in this class

    override fun onReceive(context: Context?, intent: Intent?) {
        val lastSessionStickingPoints =
            intent?.getStringExtra(AndroidNotificationScheduler.STICKING_POINTS_ID) ?: return
        val lastSessionDate = FormatService.getDate(
            intent?.getStringExtra(AndroidNotificationScheduler.DATE_ID) ?: return
        )
        val notificationService = NotificationService(context)
        notificationService.showNotification(
            lastSessionDate,
            lastSessionStickingPoints
        )
    }
}