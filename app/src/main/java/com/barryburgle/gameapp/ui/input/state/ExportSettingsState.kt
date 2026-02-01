package com.barryburgle.gameapp.ui.input.state

import com.barryburgle.gameapp.model.challenge.Challenge
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet


data class ExportSettingsState(
    var allSessions: List<AbstractSession> = emptyList(),
    var allLeads: List<Lead> = emptyList(),
    var allDates: List<Date> = emptyList(),
    var allSets: List<SingleSet> = emptyList(),
    var allChallenges: List<Challenge> = emptyList(),
    var exportSessionsFileName: String = "",
    var exportLeadsFileName: String = "",
    var exportDatesFileName: String = "",
    var exportSetsFileName: String = "",
    var exportChallengesFileName: String = "",
    var exportFolder: String = "",
    var backupFolder: String = "",
    var backupActive: Boolean = true,
    var lastBackup: Int = 3
)
