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
    val isUpdatingLead: Boolean = false,
    val leads: List<Lead> = emptyList(),
    val name: String = "",
    val contact: String = "",
    val nationality: String = "",
    val countryName: String = "",
    val age: Long = 20,
    val notificationTime: String = "",
    val editAbstractSession: AbstractSession? = null
)
