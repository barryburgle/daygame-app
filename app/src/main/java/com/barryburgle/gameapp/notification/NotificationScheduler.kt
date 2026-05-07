package com.barryburgle.gameapp.notification

import java.time.LocalDateTime

interface NotificationScheduler {
    fun schedule(
        requestCode: Int,
        time: LocalDateTime,
        title: String,
        content: String,
        interval: Int?,
    )

    fun cancel(requestCode: Int)
}