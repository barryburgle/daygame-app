package com.barryburgle.gameapp.notification.state

import java.time.LocalDateTime

data class NotificationState(
    var notificationTime: LocalDateTime,
    var lastSessionDate: String = "",
    var lastSessionStickingPoints: String = ""
)