package com.barryburgle.gameapp.ui.input

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.model.game.SortableGameEvent
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.notification.AndroidNotificationScheduler
import com.barryburgle.gameapp.notification.state.NotificationState
import com.barryburgle.gameapp.service.batch.BatchSessionService
import com.barryburgle.gameapp.service.notification.NotificationService
import com.barryburgle.gameapp.ui.CombineFourteen
import com.barryburgle.gameapp.ui.input.state.InputState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
    private val leadDao: LeadDao,
    private val dateDao: DateDao
) : ViewModel() {

    val notificationScheduler = AndroidNotificationScheduler(context)
    var notificationState: NotificationState? = null

    private val _batchSessionService = BatchSessionService()
    private val _sortType = MutableStateFlow(SortType.DATE)
    private val _allSessions = _sortType.flatMapLatest { sortType ->
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
    private val _allLeads = leadDao.getAll()
    private val _allDates = dateDao.getAll()
    private val _notificationTime = settingDao.getNotificationTime()
    private val _exportSessionsFileName = settingDao.getExportSessionsFilename()
    private val _exportLeadsFileName = settingDao.getExportLeadsFilename()
    private val _exportDatesFileName = settingDao.getExportDatesFilename()
    private val _exportFolder = settingDao.getExportFolder()
    private val _backupFolder = settingDao.getBackupFolder()
    private val _backupActive = settingDao.getBackupActiveFlag()
    private val _lastBackup = settingDao.getBackupNumber()

    val _showSessions = MutableStateFlow(true)
    val _showSets = MutableStateFlow(true)
    val _showDates = MutableStateFlow(true)
    val _allEvents: Flow<List<SortableGameEvent>> = combine(
        _showSessions,
        _showSets,
        _showDates,
        _allSessions,
        _allDates
    ) { showSessions, showSets, showDates, allSessions, allDates ->
        listOf(showSessions, showSets, showDates, allSessions, allDates)
    }.flatMapLatest { (showSessions, showSets, showDates, allSessions, allDates) ->
        val combinedList = mutableListOf<SortableGameEvent>().apply {
            if (showSessions as Boolean) addAll((allSessions as List<AbstractSession>).map {
                SortableGameEvent(
                    it.insertTime,
                    AbstractSession::class.java.simpleName,
                    it
                )
            }) else removeIf { it.classType == AbstractSession::class.java.simpleName }
            // TODO: manage sets here
            if (showDates as Boolean) addAll((allDates as List<Date>).map {
                SortableGameEvent(
                    it.insertTime,
                    Date::class.java.simpleName,
                    it
                )
            }) else removeIf { it.classType == Date::class.java.simpleName }
        }
        kotlinx.coroutines.flow.flowOf(combinedList.sortedBy { it.insertTime })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _state = MutableStateFlow(InputState())
    val state = CombineFourteen(
        _state,
        _sortType,
        _allSessions,
        _allLeads,
        _allDates,
        _allEvents,
        _notificationTime,
        _exportSessionsFileName,
        _exportLeadsFileName,
        _exportDatesFileName,
        _exportFolder,
        _backupFolder,
        _backupActive,
        _lastBackup
    ) { state, sortType, allSessions, allLeads, allDates, allEvents, notificationTime, exportSessionsFileName, exportLeadsFileName, exportDatesFileName, exportFolder, backupFolder, backupActive, lastBackup ->
        state.copy(
            allSessions = allSessions,
            allLeads = allLeads,
            allDates = allDates,
            allEvents = allEvents,
            sortType = sortType,
            notificationTime = notificationTime,
            exportSessionsFileName = exportSessionsFileName,
            exportLeadsFileName = exportLeadsFileName,
            exportDatesFileName = exportDatesFileName,
            exportFolder = exportFolder,
            backupFolder = backupFolder,
            backupActive = backupActive.toBoolean(),
            lastBackup = lastBackup.toInt()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InputState())

    fun onEvent(event: AbstractSessionEvent) {
        when (event) {

            is AbstractSessionEvent.EditSession -> {
                _state.update {
                    it.copy(
                        editAbstractSession = event.abstractSession,
                    )
                }
            }

            is AbstractSessionEvent.EditLead -> {
                _state.update {
                    if (event.isUpdatingLead) {
                        it.copy(
                            isUpdatingLead = event.isUpdatingLead,
                            leadId = event.lead.id,
                            leadInsertTime = event.lead.insertTime,
                            leadSessionId = event.lead.sessionId,
                            leadName = event.lead.name,
                            leadContact = event.lead.contact,
                            leadNationality = event.lead.nationality,
                            leadAge = event.lead.age
                        )
                    } else {
                        it.copy(
                            leadName = event.lead.name,
                            leadContact = event.lead.contact,
                            leadNationality = event.lead.nationality,
                            leadAge = event.lead.age
                        )
                    }
                }
            }

            is AbstractSessionEvent.DeleteSession -> {
                viewModelScope.launch {
                    abstractSessionDao.delete(event.abstractSession)
                }
            }

            is AbstractSessionEvent.DeleteLead -> {
                if (_state.value.isUpdatingLead) {
                    viewModelScope.launch {
                        leadDao.delete(event.lead)
                    }
                } else {
                    _state.update {
                        it.copy(
                            leads = it.leads.filter { lead -> lead.name != event.lead.name }
                        )
                    }
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
                        isAddingLead = false,
                        isModifyingLead = false,
                        isUpdatingLead = false
                    )
                }
            }

            AbstractSessionEvent.SaveAbstractSession ->
                viewModelScope.launch {
                    val abstractSession = _batchSessionService.init(
                        id = null,
                        date = if (_state.value.date.isBlank()) state.value.date else _state.value.date,
                        startHour = if (_state.value.startHour.isBlank()) state.value.startHour else _state.value.startHour,
                        endHour = if (_state.value.endHour.isBlank()) state.value.endHour else _state.value.endHour,
                        sets = if (_state.value.sets.isBlank()) state.value.sets else _state.value.sets,
                        convos = if (_state.value.convos.isBlank()) state.value.convos else _state.value.convos,
                        contacts = if (_state.value.contacts.isBlank()) state.value.contacts else _state.value.contacts,
                        stickingPoints = if (_state.value.stickingPoints.isBlank()) state.value.stickingPoints else _state.value.stickingPoints,
                    )
                    var sessionId: Long? = 0L
                    if (state.value.isAddingSession) {
                        sessionId = abstractSessionDao.insert(abstractSession)
                        notificationState = NotificationService.createNotificationState(
                            state.value.notificationTime,
                            abstractSession.date,
                            abstractSession.stickingPoints
                        )
                        notificationScheduler.schedule(notificationState!!)
                    } else if (state.value.isUpdatingSession) {
                        abstractSession.id = state.value.editAbstractSession!!.id
                        sessionId = abstractSession.id
                        abstractSession.insertTime = state.value.editAbstractSession!!.insertTime
                        abstractSessionDao.insert(abstractSession)
                    }
                    for (lead in state.value.leads) {
                        lead.insertTime = abstractSession.insertTime
                        lead.sessionId = sessionId
                        leadDao.insert(lead)
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
                            leadName = "",
                            leadContact = "",
                            leadNationality = "",
                            countryName = "",
                            leadAge = 20
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
                if (_state.value.isAddingSession) {
                    onEvent(AbstractSessionEvent.EmptyLeads)
                }
            }

            is AbstractSessionEvent.ShowLeadDialog -> {
                _state.update {
                    it.copy(
                        isAddingLead = event.addLead,
                        isModifyingLead = event.modifyLead
                    )
                }
            }

            is AbstractSessionEvent.SortSessions -> {
                _sortType.value = event.sortType
            }

            is AbstractSessionEvent.SaveLead -> {
                viewModelScope.launch {
                    leadDao.insert(event.lead)
                }
            }

            is AbstractSessionEvent.SetLead -> {
                _state.update {
                    it.copy(
                        leads = it.leads + event.lead
                    )
                }
            }

            is AbstractSessionEvent.EmptyLeads -> {
                _state.update {
                    it.copy(
                        leads = emptyList()
                    )
                }
            }

            is AbstractSessionEvent.SetLeadName -> {
                _state.update {
                    it.copy(
                        leadName = event.name
                    )
                }
            }

            is AbstractSessionEvent.SetLeadContact -> {
                _state.update {
                    it.copy(
                        leadContact = event.contact
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
                        leadNationality = event.nationality
                    )
                }
            }

            is AbstractSessionEvent.SetLeadAge -> {
                _state.update {
                    it.copy(
                        leadAge = event.age.toLong()
                    )
                }
            }

            is AbstractSessionEvent.SwitchJustSaved -> {
                _state.update {
                    it.copy(
                        justSaved = _state.value.justSaved.not()
                    )
                }
            }

            is AbstractSessionEvent.SwitchShowFlag -> {
                if (event.flagNumber == 0) {
                    _showSessions.value = !_showSessions.value
                }
                if (event.flagNumber == 1) {
                    _showSets.value = !_showSets.value
                }
                if (event.flagNumber == 2) {
                    _showDates.value = !_showDates.value
                }
            }
        }
    }
}