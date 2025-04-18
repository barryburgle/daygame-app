package com.barryburgle.gameapp.ui.input

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.model.game.SortableGameEvent
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.notification.AndroidNotificationScheduler
import com.barryburgle.gameapp.notification.state.NotificationState
import com.barryburgle.gameapp.service.batch.BatchSessionService
import com.barryburgle.gameapp.service.date.DateService
import com.barryburgle.gameapp.service.notification.NotificationService
import com.barryburgle.gameapp.ui.CombineEighteen
import com.barryburgle.gameapp.ui.CombineSix
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
    private val dateDao: DateDao,
    private val setDao: SetDao
) : ViewModel() {

    val notificationScheduler = AndroidNotificationScheduler(context)
    var notificationState: NotificationState? = null

    private val _batchSessionService = BatchSessionService()
    private val _dateService = DateService()
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
    val _allEvents: Flow<List<SortableGameEvent>> = CombineSix(
        _showSessions,
        _showSets,
        _showDates,
        _allSessions,
        _allDates,
        _allSets
    ) { showSessions, showSets, showDates, allSessions, allDates, allSets ->
        val flagsTriple = Triple(showSessions, showSets, showDates)
        listOf(flagsTriple, allSessions, allDates, allSets)
    }.flatMapLatest { (flagsTriple, allSessions, allDates, allSets) ->
        val (showSessions, showSets, showDates) = flagsTriple as Triple<Boolean, Boolean, Boolean>
        val combinedList = mutableListOf<SortableGameEvent>().apply {
            if (showSessions) addAll((allSessions as List<AbstractSession>).map {
                SortableGameEvent(
                    it.insertTime,
                    it.date,
                    AbstractSession::class.java.simpleName,
                    it
                )
            }) else removeIf { it.classType == AbstractSession::class.java.simpleName }
            if (showSets) addAll((allSets as List<SingleSet>).map {
                SortableGameEvent(
                    it.insertTime,
                    it.date,
                    SingleSet::class.java.simpleName,
                    it
                )
            }) else removeIf { it.classType == SingleSet::class.java.simpleName }
            if (showDates) addAll((allDates as List<Date>).map {
                SortableGameEvent(
                    it.insertTime,
                    it.date!!,
                    Date::class.java.simpleName,
                    it
                )
            }) else removeIf { it.classType == Date::class.java.simpleName }
        }
        kotlinx.coroutines.flow.flowOf(
            combinedList.sortedWith(
                compareByDescending<SortableGameEvent> { it.eventDate }
                    .thenByDescending { it.insertTime }
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _state = MutableStateFlow(InputState())
    val state = CombineEighteen(
        _state,
        _sortType,
        _allSessions,
        _allLeads,
        _allDates,
        _allSets,
        _allEvents,
        _notificationTime,
        _exportSessionsFileName,
        _exportLeadsFileName,
        _exportDatesFileName,
        _exportFolder,
        _backupFolder,
        _backupActive,
        _lastBackup,
        _showSessions,
        _showSets,
        _showDates
    ) { state, sortType, allSessions, allLeads, allDates, allSets, allEvents, notificationTime, exportSessionsFileName, exportLeadsFileName, exportDatesFileName, exportFolder, backupFolder, backupActive, lastBackup, showSessions, showSets, showDates ->
        state.copy(
            allSessions = allSessions,
            allLeads = allLeads,
            allDates = allDates,
            allSets = allSets,
            allEvents = allEvents,
            sortType = sortType,
            notificationTime = notificationTime,
            exportSessionsFileName = exportSessionsFileName,
            exportLeadsFileName = exportLeadsFileName,
            exportDatesFileName = exportDatesFileName,
            exportFolder = exportFolder,
            backupFolder = backupFolder,
            backupActive = backupActive.toBoolean(),
            lastBackup = lastBackup.toInt(),
            showSessions = showSessions,
            showSets = showSets,
            showDates = showDates
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InputState())

    fun onEvent(event: GameEvent) {
        when (event) {

            is GameEvent.EditSession -> {
                _state.update {
                    it.copy(
                        editAbstractSession = event.abstractSession,
                    )
                }
            }

            is GameEvent.EditLead -> {
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

            is GameEvent.DeleteSession -> {
                viewModelScope.launch {
                    abstractSessionDao.delete(event.abstractSession)
                }
            }

            is GameEvent.DeleteLead -> {
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

            is GameEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingSession = false,
                        isUpdatingSession = false,
                        isAddingDate = false,
                        isUpdatingDate = false,
                        isAddingSet = false,
                        isUpdatingSet = false
                    )
                }
            }

            is GameEvent.HideLeadDialog -> {
                _state.update {
                    it.copy(
                        isAddingLead = false,
                        isModifyingLead = false,
                        isUpdatingLead = false
                    )
                }
            }

            GameEvent.SaveAbstractSession ->
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

            is GameEvent.SetDate -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            is GameEvent.SetStartHour -> {
                _state.update {
                    it.copy(
                        startHour = event.startHour
                    )
                }
            }

            is GameEvent.SetEndHour -> {
                _state.update {
                    it.copy(
                        endHour = event.endHour
                    )
                }
            }

            is GameEvent.SetSets -> {
                _state.update {
                    it.copy(
                        sets = event.sets
                    )
                }
            }

            is GameEvent.SetConvos -> {
                _state.update {
                    it.copy(
                        convos = event.convos
                    )
                }
            }

            is GameEvent.SetContacts -> {
                _state.update {
                    it.copy(
                        contacts = event.contacts
                    )
                }
            }

            is GameEvent.SetStickingPoints -> {
                _state.update {
                    it.copy(
                        stickingPoints = event.stickingPoints
                    )
                }
            }

            is GameEvent.ShowDialog -> {
                if (EventTypeEnum.SESSION.equals(event.eventType)) {
                    _state.update {
                        it.copy(
                            isAddingSession = event.addEvent,
                            isUpdatingSession = event.updateEvent
                        )
                    }
                }
                if (EventTypeEnum.SET.equals(event.eventType)) {
                    _state.update {
                        it.copy(
                            isAddingSet = event.addEvent,
                            isUpdatingSet = event.updateEvent
                        )
                    }
                }
                if (EventTypeEnum.DATE.equals(event.eventType)) {
                    _state.update {
                        it.copy(
                            isAddingDate = event.addEvent,
                            isUpdatingDate = event.updateEvent
                        )
                    }
                }
                if (_state.value.isAddingSession) {
                    onEvent(GameEvent.EmptyLeads)
                }
            }

            is GameEvent.ShowLeadDialog -> {
                _state.update {
                    it.copy(
                        isAddingLead = event.addLead,
                        isModifyingLead = event.modifyLead
                    )
                }
            }

            is GameEvent.SortSessions -> {
                _sortType.value = event.sortType
            }

            is GameEvent.SaveLead -> {
                viewModelScope.launch {
                    leadDao.insert(event.lead)
                }
            }

            is GameEvent.SetLead -> {
                _state.update {
                    it.copy(
                        leads = it.leads + event.lead
                    )
                }
            }

            is GameEvent.EmptyLeads -> {
                _state.update {
                    it.copy(
                        leads = emptyList()
                    )
                }
            }

            is GameEvent.SetLeadName -> {
                _state.update {
                    it.copy(
                        leadName = event.name
                    )
                }
            }

            is GameEvent.SetLeadContact -> {
                _state.update {
                    it.copy(
                        leadContact = event.contact
                    )
                }
            }

            is GameEvent.SetLeadCountryName -> {
                _state.update {
                    it.copy(
                        countryName = event.countryName
                    )
                }
            }

            is GameEvent.SetLeadNationality -> {
                _state.update {
                    it.copy(
                        leadNationality = event.nationality
                    )
                }
            }

            is GameEvent.SetLeadAge -> {
                _state.update {
                    it.copy(
                        leadAge = event.age.toLong()
                    )
                }
            }

            is GameEvent.SwitchJustSaved -> {
                _state.update {
                    it.copy(
                        justSaved = _state.value.justSaved.not()
                    )
                }
            }

            is GameEvent.SwitchShowFlag -> {
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

            is GameEvent.SaveDate -> {
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
                            sortType = SortType.DATE,
                            isAddingDate = false,
                            isUpdatingDate = false
                        )
                    }
                }
            }

            is GameEvent.SetLeadId -> {
                _state.update {
                    it.copy(
                        leadId = event.leadId
                    )
                }
            }

            is GameEvent.SetLocation -> {
                _state.update {
                    it.copy(
                        location = event.location
                    )
                }
            }

            is GameEvent.SetMeetingDate -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            is GameEvent.SetCost -> {
                _state.update {
                    it.copy(
                        cost = event.cost
                    )
                }
            }

            is GameEvent.SetDateNumber -> {
                _state.update {
                    it.copy(
                        dateNumber = event.dateNumber
                    )
                }
            }

            is GameEvent.SetDateType -> {
                _state.update {
                    it.copy(
                        dateType = event.dateType
                    )
                }
            }

            is GameEvent.SwitchPull -> {
                _state.update {
                    it.copy(
                        pull = _state.value.pull.not()
                    )
                }
            }

            is GameEvent.SwitchBounce -> {
                _state.update {
                    it.copy(
                        bounce = _state.value.bounce.not()
                    )
                }
            }

            is GameEvent.SwitchKiss -> {
                _state.update {
                    it.copy(
                        kiss = _state.value.kiss.not()
                    )
                }
            }

            is GameEvent.SwitchLay -> {
                _state.update {
                    it.copy(
                        lay = _state.value.lay.not()
                    )
                }
            }

            is GameEvent.SwitchRecorded -> {
                _state.update {
                    it.copy(
                        recorded = _state.value.recorded.not()
                    )
                }
            }

            is GameEvent.SetPull -> {
                _state.update {
                    it.copy(
                        pull = event.pull!!
                    )
                }
            }

            is GameEvent.SetBounce -> {
                _state.update {
                    it.copy(
                        bounce = event.bounce!!
                    )
                }
            }

            is GameEvent.SetKiss -> {
                _state.update {
                    it.copy(
                        kiss = event.kiss!!
                    )
                }
            }

            is GameEvent.SetLay -> {
                _state.update {
                    it.copy(
                        lay = event.lay!!
                    )
                }
            }

            is GameEvent.SetRecorded -> {
                _state.update {
                    it.copy(
                        recorded = event.recorded!!
                    )
                }
            }

            is GameEvent.SetTweetUrl -> {
                _state.update {
                    it.copy(
                        tweetUrl = event.tweetUrl
                    )
                }
            }

            is GameEvent.DeleteDate -> {
                viewModelScope.launch {
                    dateDao.delete(event.date)
                }
            }

            is GameEvent.EditDate -> {
                _state.update {
                    it.copy(
                        editDate = event.date,
                    )
                }
            }

            is GameEvent.EmptyLead -> {
                _state.update {
                    it.copy(
                        lead = null
                    )
                }
            }

            is GameEvent.SortDates -> TODO("Make one sorting for all events changing by properties by event selection")
        }
    }
}