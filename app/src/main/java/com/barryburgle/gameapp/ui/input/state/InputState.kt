package com.barryburgle.gameapp.ui.input.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.ui.state.OrderState

data class InputState(
    override var allSessions: List<AbstractSession> = emptyList(),
    override var allLeads: List<Lead> = emptyList(),
    var date: String = "",
    var startHour: String = "",
    var endHour: String = "",
    var sets: String = "",
    var convos: String = "",
    var contacts: String = "",
    var stickingPoints: String = "",
    override val sortType: SortType = SortType.DATE,
    val isAddingSession: Boolean = false,
    val isUpdatingSession: Boolean = false,
    val isAddingLead: Boolean = false,
    val isModifyingLead: Boolean = false,
    val isUpdatingLead: Boolean = false,
    var leads: List<Lead> = emptyList(),
    val leadId: Long = 0L,
    val leadInsertTime: String = "",
    val leadSessionId: Long? = 0L,
    val leadName: String = "",
    val leadContact: String = "",
    val leadNationality: String = "",
    val leadAge: Long = 20,
    val countryName: String = "",
    val notificationTime: String = "",
    val editAbstractSession: AbstractSession? = null,
    override var exportSessionsFileName: String = "",
    override var exportLeadsFileName: String = "",
    override var exportDatesFileName: String = "",
    override var exportFolder: String = "",
    override var backupFolder: String = "",
    override var allDates: List<Date> = emptyList(),
    override var backupActive: Boolean = true,
    override var lastBackup: Int = 3,
    override var justSaved: Boolean = false
) : OrderState(
    sortType,
    exportSessionsFileName,
    exportLeadsFileName,
    exportDatesFileName,
    exportFolder,
    backupFolder,
    allSessions,
    allLeads,
    allDates,
    backupActive,
    lastBackup,
    justSaved
)
