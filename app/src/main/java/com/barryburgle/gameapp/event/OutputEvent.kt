package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.lead.Lead

sealed interface OutputEvent : GenericEvent {
    object SwitchShowLeadLegend : OutputEvent
    object SwitchShowIndexFormula : OutputEvent
    object SwitchShowCustomSummaryDialog : OutputEvent

    data class EditLead(val lead: Lead, val isUpdatingLead: Boolean = true) : OutputEvent
    object HideLeadDialog : OutputEvent
    data class SetLeadName(val name: String) : OutputEvent
    data class SetLeadCountrySearch(val countrySearch: String) : OutputEvent
    data class SetLeadNationality(val nationality: String) : OutputEvent
    data class SetLeadContact(val contact: String) : OutputEvent
    data class SetLeadContactLookupKey(val contactLookupKey: String) : OutputEvent
    data class SetLeadInstagramUrl(val instagramUrl: String) : OutputEvent
    data class SetLeadAge(val age: String) : OutputEvent
    data class SaveLead(val lead: Lead) : OutputEvent
    data class DeleteLead(val lead: Lead) : OutputEvent
    object SetIsInOverlayToTrue : OutputEvent
    object SetIsInOverlayToFalse : OutputEvent
}
