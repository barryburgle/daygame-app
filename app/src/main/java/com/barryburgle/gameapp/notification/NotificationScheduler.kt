package com.barryburgle.gameapp.notification

import com.barryburgle.gameapp.notification.state.ScheduledNotificationState

interface NotificationScheduler {
    fun schedule(item: ScheduledNotificationState)
    fun cancel(item: ScheduledNotificationState)
}