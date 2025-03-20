package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.model.lead.Lead

sealed interface DateEvent : GenericEvent {
    object SaveDate : DateEvent
    data class ShowDialog(val addDate: Boolean, val updateDate: Boolean) : DateEvent
    object HideDialog : DateEvent
    data class SetLeadId(val leadId: Long) : DateEvent
    data class SetLocation(val location: String) : DateEvent
    data class SetMeetingDate(val date: String) : DateEvent
    data class SetStartHour(val startHour: String) : DateEvent
    data class SetEndHour(val endHour: String) : DateEvent
    data class SetCost(val cost: String) : DateEvent
    data class SetDateNumber(val dateNumber: String) : DateEvent
    data class SetDateType(val dateType: String) : DateEvent
    object SwitchPull: DateEvent
    object SwitchBounce: DateEvent
    object SwitchKiss: DateEvent
    object SwitchLay: DateEvent
    object SwitchRecorded: DateEvent
    data class SetPull(val pull: Boolean?): DateEvent
    data class SetBounce(val bounce: Boolean?): DateEvent
    data class SetKiss(val kiss: Boolean?): DateEvent
    data class SetLay(val lay: Boolean?): DateEvent
    data class SetRecorded(val recorded: Boolean?): DateEvent
    data class SetStickingPoints(val stickingPoints: String) : DateEvent
    data class SetTweetUrl(val tweetUrl: String) : DateEvent
    data class SortDates(val sortType: DateSortType) : DateEvent
    data class DeleteDate(val date: Date) : DateEvent
    data class EditDate(val date: Date) : DateEvent
    data class EditLead(val lead: Lead, val isUpdatingLead: Boolean) : DateEvent
    object EmptyLead : DateEvent
}