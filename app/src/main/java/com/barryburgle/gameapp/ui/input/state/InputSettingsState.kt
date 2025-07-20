package com.barryburgle.gameapp.ui.input.state


data class InputSettingsState(
    val notificationTime: String = "",
    var exportSessionsFileName: String = "",
    var exportLeadsFileName: String = "",
    var exportDatesFileName: String = "",
    var exportSetsFileName: String = "",
    var exportFolder: String = "",
    var backupFolder: String = "",
    var backupActive: Boolean = true,
    var lastBackup: Int = 3,
    var generateiDate: Boolean = true,
    var followCount: Boolean = false,
    var simplePlusOneReport: Boolean = true,
    var neverShareLeadInfo: Boolean = false
)
