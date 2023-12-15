package com.barryburgle.gameapp.notification

import com.barryburgle.gameapp.notification.state.NotificationState

interface NotificationScheduler {
    fun schedule(item: NotificationState)
    fun cancel(item: NotificationState)
}