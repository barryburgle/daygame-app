package com.barryburgle.gameapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.barryburgle.gameapp.database.session.GameAppDatabase
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.service.notification.NotificationService
import kotlinx.coroutines.flow.combine

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // TODO: understand why it is not performing any action on device startup
            val database = GameAppDatabase.getInstance(context!!)
            val _notificationTime = database!!.settingDao.getNotificationTime()
            val _lastSession = database!!.abstractSessionDao.getLastSession()
            val notificationScheduler = AndroidNotificationScheduler(context)
            val state =
                combine(
                    _notificationTime,
                    _lastSession
                ) { notificationTime, lastSession ->
                    val notificationState = NotificationService
                        .createNotificationState(
                            FormatService.parseDate(lastSession.date).plusDays(1)
                                .toString() + 'T' + notificationTime,
                            lastSession.date,
                            lastSession.stickingPoints
                        )
                    notificationScheduler.schedule(notificationState!!)
                }
        }
    }
}