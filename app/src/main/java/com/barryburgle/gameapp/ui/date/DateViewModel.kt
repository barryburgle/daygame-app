package com.barryburgle.gameapp.ui.date

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.DateEvent
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.service.date.DateService
import com.barryburgle.gameapp.ui.CombineEight
import com.barryburgle.gameapp.ui.date.state.DateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DateViewModel(
    private val dateDao: DateDao,
    private val leadDao: LeadDao,
    private val abstractSessionDao: AbstractSessionDao,
    private val settingDao: SettingDao
) : ViewModel() {
    private val _state =
        MutableStateFlow(DateState())
    private val _dateService = DateService()
    private val _sortType = MutableStateFlow(DateSortType.DATE)
    private val _allDates = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            DateSortType.LEAD -> dateDao.getByLead()
            DateSortType.LOCATION -> dateDao.getByLocation()
            DateSortType.DATE -> dateDao.getByDate()
            DateSortType.COST -> dateDao.getByCost()
            DateSortType.DATE_NUMBER -> dateDao.getByDateNumber()
            DateSortType.DATE_TYPE -> dateDao.getByDateType()
            DateSortType.PULL -> dateDao.getPulled()
            DateSortType.NOT_PULL -> dateDao.getNotPulled()
            DateSortType.BOUNCE -> dateDao.getBounced()
            DateSortType.NOT_BOUNCE -> dateDao.getNotBounced()
            DateSortType.KISS -> dateDao.getKissed()
            DateSortType.NOT_KISS -> dateDao.getNotKissed()
            DateSortType.LAY -> dateDao.getLaid()
            DateSortType.NOT_LAY -> dateDao.getNotLaid()
            DateSortType.RECORD -> dateDao.getRecorded()
            DateSortType.NOT_RECORD -> dateDao.getNotRecorded()
            DateSortType.DATE_TIME -> dateDao.getByDateTime()
            DateSortType.DAY_OF_WEEK -> dateDao.getByDayOfWeek()
            DateSortType.WEEK_NUMBER -> dateDao.getByWeekNumber()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _allLeads = leadDao.getAll()
    private val _allSessions = abstractSessionDao.getAll()
    private val _lastBackup = settingDao.getBackupNumber()
    private val _exportFolder = settingDao.getExportFolder()
    private val _backupFolder = settingDao.getBackupFolder()

    val state =
        CombineEight(
            _sortType,
            _state,
            _allDates,
            _allLeads,
            _allSessions,
            _lastBackup,
            _exportFolder,
            _backupFolder
        ) { sortType, state, allDates, allLeads, allSessions, lastBackup, exportFolder, backupFolder ->
            state.copy(
                sortType = sortType,
                allDates = allDates,
                allLeads = allLeads,
                allSessions = allSessions,
                lastBackup = lastBackup.toInt(),
                exportFolder = exportFolder,
                backupFolder = backupFolder
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DateState())

    fun onEvent(event: DateEvent) {
        when (event) {
            is DateEvent.SaveDate -> {
                viewModelScope.launch {
                    val date = _dateService.init(
                        id = null,
                        leadId = if (_state.value.leadId == 0L) state.value.leadId else _state.value.leadId,
                        location = if (_state.value.location.isBlank()) state.value.location else _state.value.location,
                        date = if (_state.value.date.isBlank()) state.value.date else _state.value.date,
                        startHour = if (_state.value.startHour.isBlank()) state.value.startHour else _state.value.startHour,
                        endHour = if (_state.value.endHour.isBlank()) state.value.endHour else _state.value.endHour,
                        cost = if (_state.value.cost.isBlank()) state.value.cost.toInt() else _state.value.cost.toInt(),
                        dateNumber = if (_state.value.dateNumber.isBlank()) state.value.dateNumber.toInt() else _state.value.dateNumber.toInt(),
                        dateType = if (_state.value.dateType.isBlank()) state.value.dateType else _state.value.dateType,
                        pull = if (!_state.value.pull) state.value.pull else _state.value.pull,
                        bounce = if (!_state.value.bounce) state.value.bounce else _state.value.bounce,
                        kiss = if (!_state.value.kiss) state.value.kiss else _state.value.kiss,
                        lay = if (!_state.value.lay) state.value.lay else _state.value.lay,
                        recorded = if (!_state.value.recorded) state.value.recorded else _state.value.recorded,
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

            is DateEvent.SetPull -> {
                _state.update {
                    it.copy(
                        pull = event.pull!!
                    )
                }
            }

            is DateEvent.SetBounce -> {
                _state.update {
                    it.copy(
                        bounce = event.bounce!!
                    )
                }
            }

            is DateEvent.SetKiss -> {
                _state.update {
                    it.copy(
                        kiss = event.kiss!!
                    )
                }
            }

            is DateEvent.SetLay -> {
                _state.update {
                    it.copy(
                        lay = event.lay!!
                    )
                }
            }

            is DateEvent.SetRecorded -> {
                _state.update {
                    it.copy(
                        recorded = event.recorded!!
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