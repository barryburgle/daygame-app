package com.barryburgle.gameapp.ui.input.state


data class DialogSettingsState(
    val notificationTime: String = "",
    var generateiDate: Boolean = true,
    var followCount: Boolean = true,
    var suggestLeadsNationality: Boolean = true,
    val incrementChallengeGoal: Int = 5,
    val defaultChallengeGoal: Int = 20,
    var liveSessionNotificationEnabled: Boolean = true,
    var liveSessionSittingReminderEnabled: Boolean = true,
    var liveSessionSittingReminderInterval: Int = 30,
    var liveSessionShareEnabled: Boolean = true
)
