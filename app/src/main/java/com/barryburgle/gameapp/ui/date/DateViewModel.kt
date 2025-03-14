package com.barryburgle.gameapp.ui.date

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.event.DateEvent
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.service.date.DateService
import com.barryburgle.gameapp.ui.CombineThree
import com.barryburgle.gameapp.ui.date.state.DateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DateViewModel(
    private val dateDao: DateDao,
    private val leadDao: LeadDao
) : ViewModel() {
    private val _state =
        MutableStateFlow(DateState())
    private val _dateSortType = MutableStateFlow(DateSortType.DATE)
    private val _dateService = DateService()
    private val _sortType = MutableStateFlow(DateSortType.DATE)
    private val _dates = _dateSortType.flatMapLatest { sortType ->
        when (sortType) {
            DateSortType.DATE -> dateDao.getByDate()
            DateSortType.LEAD -> dateDao.getByLead()
            DateSortType.LOCATION -> dateDao.getByLocation()
            DateSortType.START_TIME -> dateDao.getByStartHour()
            DateSortType.END_TIME -> dateDao.getByEndHour()
            DateSortType.DATE_TIME -> dateDao.getByDate() /*TODO: COMPUTE DATE TIME ON THE FLY AND SORT IN LIST*/
            DateSortType.COST -> dateDao.getByCost()
            DateSortType.DATE_NUMBER -> dateDao.getByDateNumber()
            DateSortType.PULL -> dateDao.getPulled()
            DateSortType.NOT_PULL -> dateDao.getNotPulled()
            DateSortType.BOUNCE -> dateDao.getBounced()
            DateSortType.NOT_BOUNCE -> dateDao.getNotBounced()
            DateSortType.KISS -> dateDao.getKissed()
            DateSortType.NOT_KISS -> dateDao.getNotKissed()
            DateSortType.LAY -> dateDao.getLaid()
            DateSortType.NOT_LAY -> dateDao.getNotLaid()
            DateSortType.RECORDED -> dateDao.getRecorded()
            DateSortType.NOT_RECORDED -> dateDao.getNotRecorded()
            DateSortType.DAY_OF_WEEK -> dateDao.getByDate()/*TODO: COMPUTE ON THE FLY AND SORT IN LIST*/
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _allLeads = leadDao.getAll()

    val state =
        CombineThree(
            _state,
            _dates,
            _allLeads
        ) { state, dates, allLeads ->
            state.copy(
                dates = dates,
                allLeads = allLeads
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DateState())

    fun onEvent(event: DateEvent) {
        when (event) {
            is DateEvent.SaveDate -> {
                viewModelScope.launch {
                    val date = _dateService.init(
                        id = null,
                        leadId = _state.value.leadId,
                        location = if (_state.value.location.isBlank()) state.value.location else _state.value.location,
                        date = if (_state.value.date.isBlank()) state.value.date else _state.value.date,
                        startHour = if (_state.value.startHour.isBlank()) state.value.startHour else _state.value.startHour,
                        endHour = if (_state.value.endHour.isBlank()) state.value.endHour else _state.value.endHour,
                        cost = if (_state.value.cost.isBlank()) state.value.cost.toInt() else _state.value.cost.toInt(),
                        dateNumber = if (_state.value.dateNumber.isBlank()) state.value.dateNumber.toInt() else _state.value.dateNumber.toInt(),
                        dateType = _state.value.dateType,
                        pull = _state.value.pull,
                        bounce = _state.value.bounce,
                        kiss = _state.value.kiss,
                        lay = _state.value.lay,
                        recorded = _state.value.recorded,
                        stickingPoints = if (_state.value.stickingPoints.isBlank()) state.value.stickingPoints else _state.value.stickingPoints,
                        tweetUrl = if (_state.value.tweetUrl.isBlank()) state.value.tweetUrl else _state.value.tweetUrl
                    )
                    if (state.value.isUpdatingDate) {
                        date.id = state.value.editDate!!.id
                        date.insertTime = state.value.editDate!!.insertTime
                    }
                    dateDao.insert(date)
                    _state.update {
                        it.copy(
                            leadId = 0L,
                            location = "",
                            date = "",
                            startHour = "",
                            endHour = "",
                            cost = "0",
                            dateNumber = "0",
                            dateType = "",
                            pull = false,
                            bounce = false,
                            kiss = false,
                            lay = false,
                            recorded = false,
                            stickingPoints = "",
                            tweetUrl = "",
                            sortType = DateSortType.DATE,
                            isAddingDate = false,
                            isUpdatingDate = false
                        )
                    }
                }
            }

            is DateEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingDate = event.addDate,
                        isUpdatingDate = event.updateDate
                    )
                }
                if (_state.value.isAddingDate) {
                    onEvent(DateEvent.EmptyLead)
                }
            }

            is DateEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingDate = false,
                        isUpdatingDate = false
                    )
                }
            }

            is DateEvent.SetLeadId -> {
                _state.update {
                    it.copy(
                        leadId = event.leadId
                    )
                }
            }

            is DateEvent.SetLocation -> {
                _state.update {
                    it.copy(
                        location = event.location
                    )
                }
            }

            is DateEvent.SetMeetingDate -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            is DateEvent.SetStartHour -> {
                _state.update {
                    it.copy(
                        startHour = event.startHour
                    )
                }
            }

            is DateEvent.SetEndHour -> {
                _state.update {
                    it.copy(
                        endHour = event.endHour
                    )
                }
            }

            is DateEvent.SetCost -> {
                _state.update {
                    it.copy(
                        cost = event.cost
                    )
                }
            }

            is DateEvent.SetDateNumber -> {
                _state.update {
                    it.copy(
                        dateNumber = event.dateNumber
                    )
                }
            }

            is DateEvent.SetDateType -> {
                _state.update {
                    it.copy(
                        dateType = event.dateType
                    )
                }
            }

            is DateEvent.SwitchPull -> {
                _state.update {
                    it.copy(
                        pull = _state.value.pull.not()
                    )
                }
            }

            is DateEvent.SwitchBounce -> {
                _state.update {
                    it.copy(
                        bounce = _state.value.bounce.not()
                    )
                }
            }

            is DateEvent.SwitchKiss -> {
                _state.update {
                    it.copy(
                        kiss = _state.value.kiss.not()
                    )
                }
            }

            is DateEvent.SwitchLay -> {
                _state.update {
                    it.copy(
                        lay = _state.value.lay.not()
                    )
                }
            }

            is DateEvent.SwitchRecorded -> {
                _state.update {
                    it.copy(
                        recorded = _state.value.recorded.not()
                    )
                }
            }

            is DateEvent.SetStickingPoints -> {
                _state.update {
                    it.copy(
                        stickingPoints = event.stickingPoints
                    )
                }
            }

            is DateEvent.SetTweetUrl -> {
                _state.update {
                    it.copy(
                        tweetUrl = event.tweetUrl
                    )
                }
            }

            is DateEvent.SortDates -> {
                _sortType.value = event.sortType
            }

            is DateEvent.DeleteDate -> {
                viewModelScope.launch {
                    dateDao.delete(event.date)
                }
            }

            is DateEvent.EditDate -> {
                _state.update {
                    it.copy(
                        editDate = event.date,
                    )
                }
            }

            is DateEvent.EditLead -> {
                _state.update {
                    it.copy(
                        lead = event.lead,
                    )
                }
            }

            is DateEvent.EmptyLead -> {
                _state.update {
                    it.copy(
                        lead = null
                    )
                }
            }
        }
    }
}