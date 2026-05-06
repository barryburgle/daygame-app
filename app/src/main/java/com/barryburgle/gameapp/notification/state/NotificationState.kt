package com.barryburgle.gameapp.notification.state

import java.time.LocalDateTime

data class NotificationState(
    var time: LocalDateTime,
    var title: String = "",
    var content: String = ""
)