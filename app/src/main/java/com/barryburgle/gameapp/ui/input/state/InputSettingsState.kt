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
    var suggestLeadsNationality: Boolean = true,
    var autoSetEventDateTime: Boolean = true,
    var autoSetSessionTimeToStart: Boolean = true,
    var autoSetSetTimeToStart: Boolean = true,
    var autoSetDateTimeToStart: Boolean = true,
    var shownNationalities: Int = 3,
    var simplePlusOneReport: Boolean = true,
    var neverShareLeadInfo: Boolean = false,
    var copyReportOnClipboard: Boolean = true,
    var showSummaryCard: Boolean = false
)
