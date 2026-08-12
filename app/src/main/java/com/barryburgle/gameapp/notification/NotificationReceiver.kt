package com.barryburgle.gameapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.AlarmClock
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
        val receivedLink =
            intent.getStringExtra(AndroidNotificationScheduler.NOTIFICATION_LINK)
        if (receivedInterval!! != 0 && context != null
        ) {
            val notificationScheduler = AndroidNotificationScheduler(context)
            val time = LocalDateTime.now().plusMinutes(receivedInterval.toLong())
            notificationScheduler.schedule(
                requestCode,
                time,
                notificationTitle,
                notificationContent,
                receivedInterval,
                receivedLink
            )
        }

        val actionIntent =
            if (receivedLink == AndroidNotificationScheduler.TIMER_NOTIFICATION_LINK_VALUE) {
                Intent(AlarmClock.ACTION_SET_TIMER).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            } else if (!receivedLink.isNullOrBlank()) {
                Intent(Intent.ACTION_VIEW, Uri.parse(receivedLink)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            } else {
                context!!.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
            }

        val pendingIntent = if (actionIntent != null) {
            android.app.PendingIntent.getActivity(
                context,
                requestCode,
                actionIntent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            null
        }

        notificationService.showNotification(
            notificationTitle,
            notificationContent,
            pendingIntent
        )
    }
}