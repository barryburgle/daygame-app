package com.barryburgle.gameapp.ui.input.state


data class DialogSettingsState(
    val notificationTime: String = "",
    var generateiDate: Boolean = true,
    var followCount: Boolean = false,
    var suggestLeadsNationality: Boolean = true,
    val incrementChallengeGoal: Int = 5,
    val defaultChallengeGoal: Int = 20
)
