package com.barryburgle.gameapp.ui.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.service.batch.BatchSessionService
import com.barryburgle.gameapp.ui.input.state.InputState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class InputViewModel(private val abstractSessionDao: AbstractSessionDao) : ViewModel() {

    private val _batchSessionService = BatchSessionService()
    private val _sortType = MutableStateFlow(SortType.DATE)
    private val _abstractSessions = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType.DATE -> abstractSessionDao.getByDate()
            SortType.SETS -> abstractSessionDao.getBySets()
            SortType.CONVOS -> abstractSessionDao.getByConvos()
            SortType.CONTACS -> abstractSessionDao.getByContacts()
            SortType.SESSION_TIME -> abstractSessionDao.getBySessionTime()
            SortType.APPROACH_TIME -> abstractSessionDao.getByApproachTime()
            SortType.CONVO_RATIO -> abstractSessionDao.getByConvoRatio()
            SortType.REJECTION_RATIO -> abstractSessionDao.getByRejectionRatio()
            SortType.CONTACT_RATIO -> abstractSessionDao.getByContactRatio()
            SortType.INDEX -> abstractSessionDao.getByIndex()
            SortType.DAY_OF_WEEK -> abstractSessionDao.getByDayOfWeek()
            SortType.WEEK_NUMBER -> abstractSessionDao.getByWeekNumber()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(InputState())
    val state = combine(_state, _sortType, _abstractSessions) { state, sortType, abstractSessions ->
        state.copy(
            abstractSessions = abstractSessions,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InputState())

    fun onEvent(event: AbstractSessionEvent) {
        when (event) {

            is AbstractSessionEvent.DeleteSession -> {
                viewModelScope.launch {
                    abstractSessionDao.delete(event.abstractSession)
                }
            }

            is AbstractSessionEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingSession = false
                    )
                }
            }

            AbstractSessionEvent.SaveAbstractSession ->
                viewModelScope.launch {
                    val date = state.value.date
                    val startHour = state.value.startHour
                    val endHour = state.value.endHour
                    val sets = state.value.sets
                    val convos = state.value.convos
                    val contacts = state.value.contacts
                    val stickingPoints = state.value.stickingPoints
                    val abstractSession = _batchSessionService.init(
                        date = date,
                        startHour = startHour,
                        endHour = endHour,
                        sets = sets,
                        convos = convos,
                        contacts = contacts,
                        stickingPoints = stickingPoints
                    )
                    viewModelScope.launch { abstractSessionDao.insert(abstractSession) }
                    _state.update {
                        it.copy(
                            date = "",
                            startHour = "",
                            endHour = "",
                            sets = "",
                            convos = "",
                            contacts = "",
                            stickingPoints = "",
                            sortType = SortType.DATE,
                            isAddingSession = false
                        )
                    }
                }

            is AbstractSessionEvent.SetDate -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            is AbstractSessionEvent.SetStartHour -> {
                _state.update {
                    it.copy(
                        startHour = event.startHour
                    )
                }
            }

            is AbstractSessionEvent.SetEndHour -> {
                _state.update {
                    it.copy(
                        endHour = event.endHour
                    )
                }
            }

            is AbstractSessionEvent.SetSets -> {
                _state.update {
                    it.copy(
                        sets = event.sets
                    )
                }
            }

            is AbstractSessionEvent.SetConvos -> {
                _state.update {
                    it.copy(
                        convos = event.convos
                    )
                }
            }

            is AbstractSessionEvent.SetContacts -> {
                _state.update {
                    it.copy(
                        contacts = event.contacts
                    )
                }
            }

            is AbstractSessionEvent.SetStickingPoints -> {
                _state.update {
                    it.copy(
                        stickingPoints = event.stickingPoints
                    )
                }
            }

            is AbstractSessionEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingSession = true
                    )
                }
            }

            is AbstractSessionEvent.SortSessions -> {
                _sortType.value = event.sortType
            }
        }
    }
}