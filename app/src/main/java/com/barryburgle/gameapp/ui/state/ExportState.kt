package com.barryburgle.gameapp.ui.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.FieldEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet

open class ExportState(
    open val sortType: FieldEnum? = null,
    open var exportSessionsFileName: String = "",
    open var exportLeadsFileName: String = "",
    open var exportDatesFileName: String = "",
    open var exportSetsFileName: String = "",
    open var exportFolder: String = "",
    open var backupFolder: String = "",
    override var allSessions: List<AbstractSession> = emptyList(),
    override var allLeads: List<Lead> = emptyList(),
    override var allDates: List<Date> = emptyList(),
    override var allSets: List<SingleSet> = emptyList(),
    open var backupActive: Boolean = true,
    open var lastBackup: Int = 3,
    open var justSaved: Boolean = false,
    open var generateiDate: Boolean = true
) : AllEntityState(
    allSessions,
    allLeads,
    allDates,
    allSets,
)