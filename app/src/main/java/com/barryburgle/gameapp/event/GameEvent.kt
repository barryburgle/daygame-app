package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.enums.SetSortType
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet

sealed interface GameEvent : GenericEvent {
    object SaveAbstractSession : GameEvent
    data class ShowDialog(
        val addEvent: Boolean,
        val updateEvent: Boolean,
        val eventType: EventTypeEnum
    ) :
        GameEvent

    class ShowLeadDialog(val addLead: Boolean, val modifyLead: Boolean) : GameEvent
    object HideDialog : GameEvent
    object HideLeadDialog : GameEvent
    data class SetDate(val date: String) : GameEvent
    data class SetStartHour(val startHour: String) : GameEvent
    data class SetEndHour(val endHour: String) : GameEvent
    data class SetSets(val sets: String) : GameEvent
    data class SetConvos(val convos: String) : GameEvent
    data class SetContacts(val contacts: String) : GameEvent
    data class SetStickingPoints(val stickingPoints: String) : GameEvent
    data class SortSessions(val sortType: SortType) : GameEvent
    data class DeleteSession(val abstractSession: AbstractSession) : GameEvent
    data class DeleteLead(val lead: Lead) : GameEvent
    data class EditSession(val abstractSession: AbstractSession) : GameEvent
    data class EditLead(val lead: Lead, val isUpdatingLead: Boolean) : GameEvent
    data class SaveLead(val lead: Lead) : GameEvent
    data class SetLead(val lead: Lead) : GameEvent
    object EmptyLeads : GameEvent
    data class SetLeadName(val name: String) : GameEvent
    data class SetLeadContact(val contact: String) : GameEvent
    data class SetLeadCountryName(val countryName: String) : GameEvent
    data class SetLeadNationality(val nationality: String) : GameEvent
    data class SetLeadAge(val age: String) : GameEvent
    object SwitchJustSaved : GameEvent
    data class SwitchShowFlag(val flagNumber: Int) : GameEvent

    object SaveDate : GameEvent
    data class SetLeadId(val leadId: Long) : GameEvent
    data class SetLocation(val location: String) : GameEvent
    data class SetCost(val cost: String) : GameEvent
    data class SetDateNumber(val dateNumber: String) : GameEvent
    data class SetDateType(val dateType: String) : GameEvent
    object SwitchPull : GameEvent
    object SwitchBounce : GameEvent
    object SwitchKiss : GameEvent
    object SwitchLay : GameEvent
    object SwitchRecorded : GameEvent
    data class SetPull(val pull: Boolean?) : GameEvent
    data class SetBounce(val bounce: Boolean?) : GameEvent
    data class SetKiss(val kiss: Boolean?) : GameEvent
    data class SetLay(val lay: Boolean?) : GameEvent
    data class SetRecorded(val recorded: Boolean?) : GameEvent
    data class SetTweetUrl(val tweetUrl: String) : GameEvent
    data class SortDates(val sortType: DateSortType) : GameEvent
    data class DeleteDate(val date: Date) : GameEvent
    data class EditDate(val date: Date) : GameEvent
    object EmptyLead : GameEvent

    object SaveSet : GameEvent
    data class SetSessionId(val sessionId: Long) : GameEvent
    data class SetDateId(val dateId: Long) : GameEvent
    object SwitchConversation : GameEvent
    object SwitchContact : GameEvent
    object SwitchInstantDate : GameEvent
    data class SortSets(val sortType: SetSortType) : GameEvent
    data class DeleteSet(val set: SingleSet) : GameEvent
    data class EditSet(val set: SingleSet) : GameEvent
}