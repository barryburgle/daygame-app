package com.barryburgle.gameapp.ui.input.state


data class ShareSettingsState(
    var shownNationalities: Int = 3,
    var simplePlusOneReport: Boolean = true,
    var neverShareLeadInfo: Boolean = false,
    var copyReportOnClipboard: Boolean = true,
    var showSummaryCard: Boolean = false
)
