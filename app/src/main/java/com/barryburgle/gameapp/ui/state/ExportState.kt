package com.barryburgle.gameapp.ui.state

import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.FieldEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.session.PinPoint
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.model.setting.Setting

open class ExportState(
    open val sortType: FieldEnum? = null,
    open var exportSessionsFileName: String = "",
    open var exportLeadsFileName: String = "",
    open var exportDatesFileName: String = "",
    open var exportSetsFileName: String = "",
    open var exportChallengesFileName: String = "",
    open var exportPinPointsFileName: String = "",
    open var exportSettingsFileName: String = "",
    open var exportFolder: String = "",
    open var backupFolder: String = "",
    override var allSessions: List<AbstractSession> = emptyList(),
    override var allLeads: List<Lead> = emptyList(),
    override var allDates: List<Date> = emptyList(),
    override var allSets: List<SingleSet> = emptyList(),
    override var allChallenges: List<AchievedChallenge> = emptyList(),
    override var allPinPoints: List<PinPoint> = emptyList(),
    override var allSettings: List<Setting> = emptyList(),
    open var backupActive: Boolean = true,
    open var lastBackup: Int = 3,
    open var justSaved: Boolean = false,
    open var pinPointInteractions: Boolean = true,
    open var generateiDate: Boolean = true,
    open var liveSessionNotificationEnabled: Boolean = true,
    open var liveSessionSittingReminderEnabled: Boolean = true,
    open var liveSessionSittingReminderInterval: Int = 30,
    open var liveSessionShareEnabled: Boolean = true,
    open var writeHerAfterReminderEnabled: Boolean = true,
    open var writeHerReminderInterval: Int = 60

) : AllEntityState(
    allSessions,
    allLeads,
    allDates,
    allSets,
)