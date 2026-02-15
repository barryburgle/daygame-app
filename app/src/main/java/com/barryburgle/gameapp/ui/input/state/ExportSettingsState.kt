package com.barryburgle.gameapp.ui.input.state

import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.model.setting.Setting


data class ExportSettingsState(
    var allSessions: List<AbstractSession> = emptyList(),
    var allLeads: List<Lead> = emptyList(),
    var allDates: List<Date> = emptyList(),
    var allSets: List<SingleSet> = emptyList(),
    var allChallenges: List<AchievedChallenge> = emptyList(),
    var allSettings: List<Setting> = emptyList(),
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
