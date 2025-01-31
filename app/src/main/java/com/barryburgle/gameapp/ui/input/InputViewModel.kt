package com.barryburgle.gameapp.ui.input

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.notification.AndroidNotificationScheduler
import com.barryburgle.gameapp.notification.state.NotificationState
import com.barryburgle.gameapp.service.batch.BatchSessionService
import com.barryburgle.gameapp.service.notification.NotificationService
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
class InputViewModel(
    private val context: Context,
    private val abstractSessionDao: AbstractSessionDao,
    private val settingDao: SettingDao,
    private val leadDao: LeadDao
) : ViewModel() {

    val notificationScheduler = AndroidNotificationScheduler(context)
    var notificationState: NotificationState? = null

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
    private val _notificationTime = settingDao.getNotificationTime()
    private val _state = MutableStateFlow(InputState())
    val state = combine(
        _state,
        _sortType,
        _abstractSessions,
        _notificationTime
    ) { state, sortType, abstractSessions, notificationTime ->
        state.copy(
            abstractSessions = abstractSessions,
            sortType = sortType,
            notificationTime = notificationTime
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InputState())

    fun onEvent(event: AbstractSessionEvent) {
        when (event) {

            is AbstractSessionEvent.EditSession -> {
                _state.update {
                    it.copy(
                        editAbstractSession = event.abstractSession
                    )
                }
            }

            is AbstractSessionEvent.DeleteSession -> {
                viewModelScope.launch {
                    abstractSessionDao.delete(event.abstractSession)
                }
            }

            is AbstractSessionEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingSession = false,
                        isUpdatingSession = false
                    )
                }
            }

            is AbstractSessionEvent.HideLeadDialog -> {
                _state.update {
                    it.copy(
                        isAddingLead = false
                    )
                }
            }

            AbstractSessionEvent.SaveAbstractSession ->
                viewModelScope.launch {
                    val abstractSession = _batchSessionService.init(
                        date = if (_state.value.date.isBlank()) state.value.date else _state.value.date,
                        startHour = if (_state.value.startHour.isBlank()) state.value.startHour else _state.value.startHour,
                        endHour = if (_state.value.endHour.isBlank()) state.value.endHour else _state.value.endHour,
                        sets = if (_state.value.sets.isBlank()) state.value.sets else _state.value.sets,
                        convos = if (_state.value.convos.isBlank()) state.value.convos else _state.value.convos,
                        contacts = if (_state.value.contacts.isBlank()) state.value.contacts else _state.value.contacts,
                        stickingPoints = if (_state.value.stickingPoints.isBlank()) state.value.stickingPoints else _state.value.stickingPoints,
                    )
                    if (state.value.isAddingSession) {
                        var sessionId: Long? = abstractSessionDao.insert(abstractSession)
                        for (lead in state.value.leads) {
                            lead.insertTime = abstractSession.insertTime
                            lead.sessionId = sessionId
                            leadDao.insert(lead)
                        }
                        notificationState = NotificationService.createNotificationState(
                            state.value.notificationTime,
                            abstractSession.date,
                            abstractSession.stickingPoints
                        )
                        //notificationScheduler.schedule(notificationState!!)
                    } else if (state.value.isUpdatingSession) {
                        abstractSession.id = state.value.editAbstractSession!!.id
                        abstractSession.insertTime = state.value.editAbstractSession!!.insertTime
                        abstractSessionDao.insert(abstractSession)
                    }
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
                            isAddingSession = false,
                            isUpdatingSession = false,
                            isAddingLead = false,
                            leads = emptyList(),
                            name = "",
                            contact = "",
                            nationality = "",
                            countryName = "",
                            age = 20
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
                        isAddingSession = event.addSession,
                        isUpdatingSession = event.updateSession
                    )
                }
            }

            is AbstractSessionEvent.ShowLeadDialog -> {
                _state.update {
                    it.copy(
                        isAddingLead = true
                    )
                }
            }

            is AbstractSessionEvent.SortSessions -> {
                _sortType.value = event.sortType
            }

            is AbstractSessionEvent.SetLead -> {
                _state.update {
                    it.copy(
                        leads = it.leads + event.lead
                    )
                }
            }

            is AbstractSessionEvent.SetLeadName -> {
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }

            is AbstractSessionEvent.SetLeadContact -> {
                _state.update {
                    it.copy(
                        contact = event.contact
                    )
                }
            }

            is AbstractSessionEvent.SetLeadCountryName -> {
                _state.update {
                    it.copy(
                        countryName = event.countryName
                    )
                }
            }

            is AbstractSessionEvent.SetLeadNationality -> {
                _state.update {
                    it.copy(
                        nationality = event.nationality
                    )
                }
            }

            is AbstractSessionEvent.SetLeadAge -> {
                _state.update {
                    it.copy(
                        age = event.age.toLong()
                    )
                }
            }
        }
    }
}