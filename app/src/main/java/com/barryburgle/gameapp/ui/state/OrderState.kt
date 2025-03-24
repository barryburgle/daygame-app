package com.barryburgle.gameapp.ui.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.FieldEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession

open class OrderState(
    open val sortType: FieldEnum? = null,
    // TODO: remove this OrderState class and integrate fields in InputState after Date screen collapse in Input state
    // The following properties should be part of a BackupState.
    open var exportSessionsFileName: String = "",
    open var exportLeadsFileName: String = "",
    open var exportDatesFileName: String = "",
    open var exportFolder: String = "",
    open var backupFolder: String = "",
    open var allSessions: List<AbstractSession> = emptyList(),
    open var allLeads: List<Lead> = emptyList(),
    open var allDates: List<Date> = emptyList(),
    open var backupActive: Boolean = true
)