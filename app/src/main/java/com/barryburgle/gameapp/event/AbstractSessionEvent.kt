package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession

sealed interface AbstractSessionEvent: GenericEvent {
    object SaveAbstractSession : AbstractSessionEvent
    data class ShowDialog(val addSession: Boolean, val updateSession: Boolean) :
        AbstractSessionEvent
    class ShowLeadDialog(val addLead: Boolean, val modifyLead: Boolean) :AbstractSessionEvent
    object HideDialog : AbstractSessionEvent
    object HideLeadDialog : AbstractSessionEvent
    data class SetDate(val date: String) : AbstractSessionEvent
    data class SetStartHour(val startHour: String) : AbstractSessionEvent
    data class SetEndHour(val endHour: String) : AbstractSessionEvent
    data class SetSets(val sets: String) : AbstractSessionEvent
    data class SetConvos(val convos: String) : AbstractSessionEvent
    data class SetContacts(val contacts: String) : AbstractSessionEvent
    data class SetStickingPoints(val stickingPoints: String) : AbstractSessionEvent
    data class SortSessions(val sortType: SortType) : AbstractSessionEvent
    data class DeleteSession(val abstractSession: AbstractSession) : AbstractSessionEvent
    data class DeleteLead(val lead: Lead) : AbstractSessionEvent
    data class EditSession(val abstractSession: AbstractSession) : AbstractSessionEvent
    data class EditLead(val lead: Lead, val isUpdatingLead: Boolean) : AbstractSessionEvent
    data class SaveLead(val lead: Lead) : AbstractSessionEvent
    data class SetLead(val lead: Lead) : AbstractSessionEvent
    object EmptyLeads: AbstractSessionEvent
    data class SetLeadName(val name: String) : AbstractSessionEvent
    data class SetLeadContact(val contact: String) : AbstractSessionEvent
    data class SetLeadCountryName(val countryName: String) : AbstractSessionEvent
    data class SetLeadNationality(val nationality: String) : AbstractSessionEvent
    data class SetLeadAge(val age: String) : AbstractSessionEvent
}