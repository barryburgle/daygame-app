package com.barryburgle.gameapp.notification

import java.time.LocalDateTime

interface NotificationScheduler {
    fun schedule(
        time: LocalDateTime,
        title: String,
        content: String
    )
}