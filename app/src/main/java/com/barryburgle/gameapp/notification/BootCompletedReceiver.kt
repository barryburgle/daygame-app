package com.barryburgle.gameapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.barryburgle.gameapp.database.GameAppDatabase
import com.barryburgle.gameapp.service.FormatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED && context != null) {
            val pendingResult = goAsync()
            val database = GameAppDatabase.getInstance(context)
            if (database == null) {
                pendingResult.finish()
                return
            }
            val notificationScheduler = AndroidNotificationScheduler(context)
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                try {
                    val notificationTime = database.settingDao.getNotificationTime().first()
                    val lastSession = database.abstractSessionDao.getLastSession().first()
                    notificationScheduler.schedule(
                        LocalDateTime.parse(
                            FormatService.parseDate(lastSession.date).plusDays(1)
                                .toString() + 'T' + notificationTime
                        ),
                        lastSession.date,
                        lastSession.stickingPoints,
                        null
                    )
                } catch (e: Exception) {
                    Log.e("BootReceiver", "Error scheduling: ${e.message}")
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}