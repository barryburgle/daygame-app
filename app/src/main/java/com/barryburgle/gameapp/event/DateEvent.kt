package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateSortType

sealed interface DateEvent : GenericEvent {
    object SaveDate : DateEvent
    data class ShowDialog(val addDate: Boolean, val updateDate: Boolean) : DateEvent
    object HideDialog : DateEvent
    data class SetLeadId(val leadId: Long) : DateEvent
    data class SetLocation(val location: String) : DateEvent
    data class SetMeetingDate(val date: String) : DateEvent
    data class SetStartTime(val startTime: String) : DateEvent
    data class SetEndTime(val endHour: String) : DateEvent
    data class SetCost(val cost: Double) : DateEvent
    data class SetDateNumber(val dateNumber: Long) : DateEvent
    data class SetPull(val pull: Boolean) : DateEvent
    data class SetBounce(val bounce: Boolean) : DateEvent
    data class SetKiss(val kiss: Boolean) : DateEvent
    data class SetLay(val lay: Boolean) : DateEvent
    data class SetRecorded(val recorded: Boolean) : DateEvent
    data class SetStickingPoints(val stickingPoints: String) : DateEvent
    data class SetTweetUrl(val tweetUrl: String) : DateEvent
    data class SortDates(val sortType: DateSortType) : DateEvent
    data class DeleteDate(val date: Date) : DateEvent
    data class EditDate(val date: Date) : DateEvent
}