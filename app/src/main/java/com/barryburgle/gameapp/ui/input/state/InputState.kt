package com.barryburgle.gameapp.ui.input.state

import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession

data class InputState(
    val abstractSessions: List<AbstractSession> = emptyList(),
    val allLeads: List<Lead> = emptyList(),
    var date: String = "",
    var startHour: String = "",
    var endHour: String = "",
    var sets: String = "",
    var convos: String = "",
    var contacts: String = "",
    var stickingPoints: String = "",
    val sortType: SortType = SortType.DATE,
    val isAddingSession: Boolean = false,
    val isUpdatingSession: Boolean = false,
    val isAddingLead: Boolean = false,
    val isModifyingLead: Boolean = false,
    val isUpdatingLead: Boolean = false,
    val leads: List<Lead> = emptyList(),
    val leadId: Long = 0L,
    val leadInsertTime: String = "",
    val leadSessionId: Long? = 0L,
    val leadName: String = "",
    val leadContact: String = "",
    val leadNationality: String = "",
    val leadAge: Long = 20,
    val countryName: String = "",
    val notificationTime: String = "",
    val editAbstractSession: AbstractSession? = null
)
