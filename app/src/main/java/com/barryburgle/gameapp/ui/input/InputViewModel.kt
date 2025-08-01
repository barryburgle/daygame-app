package com.barryburgle.gameapp.ui.input

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.set.SetDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.model.enums.DateType
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.enums.GameEventSortType
import com.barryburgle.gameapp.model.enums.SessionSortType
import com.barryburgle.gameapp.model.enums.SetSortType
import com.barryburgle.gameapp.model.game.SortableGameEvent
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.notification.AndroidNotificationScheduler
import com.barryburgle.gameapp.notification.state.NotificationState
import com.barryburgle.gameapp.service.batch.BatchSessionService
import com.barryburgle.gameapp.service.date.DateService
import com.barryburgle.gameapp.service.notification.NotificationService
import com.barryburgle.gameapp.service.set.SetService
import com.barryburgle.gameapp.ui.CombineFifteen
import com.barryburgle.gameapp.ui.CombineSeven
import com.barryburgle.gameapp.ui.input.state.InputSettingsState
import com.barryburgle.gameapp.ui.input.state.InputState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
    private val _setService = SetService()
    private val _sessionSortType = MutableStateFlow(SessionSortType.DATE)
    private val _allSessions = _sessionSortType.flatMapLatest { sortType ->
        when (sortType) {
            SessionSortType.DATE -> abstractSessionDao.getByDate()
            SessionSortType.SETS -> abstractSessionDao.getBySets()
            SessionSortType.CONVOS -> abstractSessionDao.getByConvos()
            SessionSortType.CONTACS -> abstractSessionDao.getByContacts()
            SessionSortType.SESSION_TIME -> abstractSessionDao.getBySessionTime()
            SessionSortType.APPROACH_TIME -> abstractSessionDao.getByApproachTime()
            SessionSortType.CONVO_RATIO -> abstractSessionDao.getByConvoRatio()
            SessionSortType.REJECTION_RATIO -> abstractSessionDao.getByRejectionRatio()
            SessionSortType.CONTACT_RATIO -> abstractSessionDao.getByContactRatio()
            SessionSortType.INDEX -> abstractSessionDao.getByIndex()
            SessionSortType.DAY_OF_WEEK -> abstractSessionDao.getByDayOfWeek()
            SessionSortType.WEEK_NUMBER -> abstractSessionDao.getByWeekNumber()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _allLeads = leadDao.getAll()
    private val _dateSortType = MutableStateFlow(DateSortType.DATE)
    private val _allDates = _dateSortType.flatMapLatest { sortType ->
        when (sortType) {
            DateSortType.DATE -> dateDao.getByDate()
            DateSortType.LEAD -> dateDao.getByLead()
            DateSortType.LOCATION -> dateDao.getByLocation()
            DateSortType.DATE_TIME -> dateDao.getByDateTime()
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
            DateSortType.DAY_OF_WEEK -> dateDao.getByDayOfWeek()
            DateSortType.WEEK_NUMBER -> dateDao.getByWeekNumber()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _setSortType = MutableStateFlow(SetSortType.DATE)
    private val _allSets = _setSortType.flatMapLatest { sortType ->
        when (sortType) {
            SetSortType.DATE -> setDao.getByDate()
            SetSortType.START_HOUR -> setDao.getByStartHour()
            SetSortType.SET_TIME -> setDao.getBySetTime()
            SetSortType.LOCATION -> setDao.getByLocation()
            SetSortType.LEAD -> setDao.getByLead()
            SetSortType.CONVERSATION -> setDao.getConversed()
            SetSortType.NO_CONVERSATION -> setDao.getNotConversed()
            SetSortType.CONTACT -> setDao.getContact()
            SetSortType.NO_CONTACT -> setDao.getNotContact()
            SetSortType.INSTANT_DATE -> setDao.getInstantDated()
            SetSortType.NO_INSTANT_DATE -> setDao.getNotInstantDated()
            SetSortType.RECORDED -> setDao.getRecorded()
            SetSortType.NOT_RECORDED -> setDao.getNotRecorded()
            SetSortType.DAY_OF_WEEK -> setDao.getByDayOfWeek()
            SetSortType.WEEK_NUMBER -> setDao.getByWeekNumber()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _notificationTime = settingDao.getNotificationTime()
    private val _exportSessionsFileName = settingDao.getExportSessionsFilename()
    private val _exportLeadsFileName = settingDao.getExportLeadsFilename()
    private val _exportDatesFileName = settingDao.getExportDatesFilename()
    private val _exportSetsFileName = settingDao.getExportSetsFilename()
    private val _exportFolder = settingDao.getExportFolder()
    private val _backupFolder = settingDao.getBackupFolder()
    private val _backupActive = settingDao.getBackupActiveFlag()
    private val _lastBackup = settingDao.getBackupNumber()
    private val _generateiDate = settingDao.getGenerateiDate()
    private val _followCount = settingDao.getFollowCount()
    private val _suggestLeadsNationality = settingDao.getSuggestLeadsNationality()
    private val _shownNationalities = settingDao.getShownNationalities()
    private val _simplePlusOneReport = settingDao.getSimplePlusOneReport()
    private val _neverShareLeadInfo = settingDao.getNeverShareLeadInfo()

    val _showSessions = MutableStateFlow(true)
    val _showSets = MutableStateFlow(true)
    val _showDates = MutableStateFlow(true)
    private val _gameEventSortType = MutableStateFlow(GameEventSortType.DATE)
    val _allEvents: Flow<List<SortableGameEvent>> = CombineSeven(
        _showSessions,
        _showSets,
        _showDates,
        _allSessions,
        _allDates,
        _allSets,
        _gameEventSortType
    ) { showSessions, showSets, showDates, allSessions, allDates, allSets, gameEventSortType ->
        val flagsTriple = Triple(showSessions, showSets, showDates)
        listOf(flagsTriple, allSessions, allDates, allSets, gameEventSortType)
    }.flatMapLatest { (flagsTriple, allSessions, allDates, allSets, gameEventSortType) ->
        val (showSessions, showSets, showDates) = flagsTriple as Triple<Boolean, Boolean, Boolean>
        val combinedList = mutableListOf<SortableGameEvent>().apply {
            if (showSessions) addAll((allSessions as List<AbstractSession>).map {
                SortableGameEvent(
                    it.insertTime,
                    it.date,
                    it.startHour,
                    it.sessionTime,
                    it.dayOfWeek,
                    AbstractSession::class.java.simpleName,
                    it
                )
            }) else removeIf { it.classType == AbstractSession::class.java.simpleName }
            if (showSets) addAll((allSets as List<SingleSet>).map {
                SortableGameEvent(
                    it.insertTime,
                    it.date,
                    it.startHour,
                    it.setTime,
                    it.dayOfWeek,
                    SingleSet::class.java.simpleName,
                    it
                )
            }) else removeIf { it.classType == SingleSet::class.java.simpleName }
            if (showDates) addAll((allDates as List<Date>).map {
                SortableGameEvent(
                    it.insertTime,
                    it.date!!,
                    it.startHour,
                    it.dateTime,
                    it.dayOfWeek,
                    Date::class.java.simpleName,
                    it
                )
            }) else removeIf { it.classType == Date::class.java.simpleName }
        }
        if ((showSessions && !showDates && !showSets) || (!showSessions && showDates && !showSets) || (!showSessions && !showDates && showSets)) {
            flowOf(
                combinedList
            )
        } else {
            when (gameEventSortType) {
                GameEventSortType.DATE -> {
                    flowOf(combinedList.sortedWith(
                        compareByDescending<SortableGameEvent> { it.eventDate }
                    )
                    )
                }

                GameEventSortType.START_HOUR -> {
                    flowOf(combinedList.sortedWith(
                        compareByDescending<SortableGameEvent> { it.eventHour }
                    )
                    )
                }

                GameEventSortType.TIME_SPENT -> {
                    flowOf(combinedList.sortedWith(
                        compareByDescending<SortableGameEvent> { it.timeSpent }
                    )
                    )
                }

                GameEventSortType.DAY_OF_WEEK -> {
                    flowOf(combinedList.sortedWith(
                        compareByDescending<SortableGameEvent> { it.dayOfWeek }
                    )
                    )
                }

                else -> {
                    flowOf(combinedList.sortedWith(
                        compareByDescending<SortableGameEvent> { it.eventDate }
                            .thenByDescending { it.insertTime }
                    )
                    )
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _state = MutableStateFlow(InputState())
    val _combinedSettings = CombineFifteen(
        _notificationTime,
        _exportSessionsFileName,
        _exportLeadsFileName,
        _exportDatesFileName,
        _exportSetsFileName,
        _exportFolder,
        _backupFolder,
        _backupActive,
        _lastBackup,
        _generateiDate,
        _followCount,
        _suggestLeadsNationality,
        _shownNationalities,
        _simplePlusOneReport,
        _neverShareLeadInfo
    ) { notificationTime, exportSessionsFileName, exportLeadsFileName, exportDatesFileName, exportSetsFileName, exportFolder, backupFolder, backupActive, lastBackup, generateiDate, followCount, suggestLeadsNationality, shownNationalities, simplePlusOneReport, neverShareLeadInfo ->
        InputSettingsState(
            notificationTime = notificationTime,
            exportSessionsFileName = exportSessionsFileName,
            exportLeadsFileName = exportLeadsFileName,
            exportDatesFileName = exportDatesFileName,
            exportSetsFileName = exportSetsFileName,
            exportFolder = exportFolder,
            backupFolder = backupFolder,
            backupActive = backupActive.toBoolean(),
            lastBackup = lastBackup.toInt(),
            generateiDate = generateiDate.toBoolean(),
            followCount = followCount.toBoolean(),
            suggestLeadsNationality = suggestLeadsNationality.toBoolean(),
            shownNationalities = shownNationalities.toInt(),
            simplePlusOneReport = simplePlusOneReport.toBoolean(),
            neverShareLeadInfo = neverShareLeadInfo.toBoolean()
        )
    }
    val _mostPopularLeadsNationalities = leadDao.getNationalityHistogram()
    val state = CombineFifteen(
        _state,
        _allSessions,
        _allLeads,
        _allDates,
        _sessionSortType,
        _dateSortType,
        _setSortType,
        _allSets,
        _allEvents,
        _showSessions,
        _showSets,
        _showDates,
        _gameEventSortType,
        _combinedSettings,
        _mostPopularLeadsNationalities
    ) { state, allSessions, allLeads, allDates, sessionSortType, dateSortType, setSortType, allSets, allEvents, showSessions, showSets, showDates, gameEventSortType, combinedSettings, mostPopularLeadsNationalities ->
        state.copy(
            allSessions = allSessions,
            allLeads = allLeads,
            allDates = allDates,
            allSets = allSets,
            allEvents = allEvents,
            sessionSortType = sessionSortType,
            dateSortType = dateSortType,
            setSortType = setSortType,
            notificationTime = combinedSettings.notificationTime,
            exportSessionsFileName = combinedSettings.exportSessionsFileName,
            exportLeadsFileName = combinedSettings.exportLeadsFileName,
            exportDatesFileName = combinedSettings.exportDatesFileName,
            exportSetsFileName = combinedSettings.exportSetsFileName,
            exportFolder = combinedSettings.exportFolder,
            backupFolder = combinedSettings.backupFolder,
            backupActive = combinedSettings.backupActive,
            lastBackup = combinedSettings.lastBackup,
            showSessions = showSessions,
            showSets = showSets,
            showDates = showDates,
            gameEventSortType = gameEventSortType,
            generateiDate = combinedSettings.generateiDate,
            followCount = combinedSettings.followCount,
            suggestLeadsNationality = combinedSettings.suggestLeadsNationality,
            shownNationalities = combinedSettings.shownNationalities,
            simplePlusOneReport = combinedSettings.simplePlusOneReport,
            neverShareLeadInfo = combinedSettings.neverShareLeadInfo,
            mostPopularLeadsNationalities = mostPopularLeadsNationalities
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
                            sessionSortType = SessionSortType.DATE,
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
                if (state.value.followCount) {
                    var sets = 0
                    if (_state.value.sets.isEmpty()) {
                        if (_state.value.editAbstractSession != null) {
                            sets = _state.value.editAbstractSession!!.sets
                        }
                    } else {
                        sets = _state.value.sets.toInt()
                    }
                    sets++
                    _state.update {
                        it.copy(
                            sets = sets.toString()
                        )
                    }
                }
            }

            is GameEvent.SetContacts -> {
                _state.update {
                    it.copy(
                        contacts = event.contacts
                    )
                }
                if (state.value.followCount) {
                    var sets = 0
                    if (_state.value.sets.isEmpty()) {
                        if (_state.value.editAbstractSession != null) {
                            sets = _state.value.editAbstractSession!!.sets
                        }
                    } else {
                        sets = _state.value.sets.toInt()
                    }
                    sets++
                    _state.update {
                        it.copy(
                            sets = sets.toString()
                        )
                    }
                    var convos = 0
                    if (_state.value.convos.isEmpty()) {
                        if (_state.value.editAbstractSession != null) {
                            convos = _state.value.editAbstractSession!!.convos
                        }
                    } else {
                        convos = _state.value.convos.toInt()
                    }
                    convos++
                    _state.update {
                        it.copy(
                            convos = convos.toString()
                        )
                    }
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
                _sessionSortType.value = event.sessionSortType
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
                            dateSortType = DateSortType.DATE,
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

            is GameEvent.SortDates -> {
                _dateSortType.value = event.sortType
            }

            is GameEvent.SaveSet -> {
                viewModelScope.launch {
                    val isAddingSet = state.value.isAddingSet
                    val isUpdatingSet = state.value.isUpdatingSet
                    var dateId: Long? = null
                    if (isAddingSet && !isUpdatingSet && _state.value.generateiDate && _state.value.instantDate) {
                        val date = _dateService.init(
                            id = null,
                            leadId = if (_state.value.leadId == 0L) state.value.leadId else _state.value.leadId,
                            location = if (_state.value.location.isBlank()) state.value.location else _state.value.location,
                            date = if (_state.value.date.isBlank()) state.value.date else _state.value.date,
                            startHour = if (_state.value.startHour.isBlank()) state.value.startHour else _state.value.startHour,
                            endHour = if (_state.value.endHour.isBlank()) state.value.endHour else _state.value.endHour,
                            cost = 0,
                            dateNumber = 0,
                            dateType = DateType.DRINK.getType(),
                            pull = false,
                            bounce = false,
                            kiss = false,
                            lay = false,
                            recorded = if (!_state.value.recorded) state.value.recorded else _state.value.recorded,
                            stickingPoints = if (_state.value.stickingPoints.isBlank()) state.value.stickingPoints else _state.value.stickingPoints,
                            tweetUrl = if (_state.value.tweetUrl.isBlank()) state.value.tweetUrl else _state.value.tweetUrl
                        )
                        dateId = dateDao.insert(date)
                    }
                    val set = _setService.init(
                        id = null,
                        date = if (_state.value.date.isBlank()) state.value.date else _state.value.date,
                        startHour = if (_state.value.startHour.isBlank()) state.value.startHour else _state.value.startHour,
                        endHour = if (_state.value.endHour.isBlank()) state.value.endHour else _state.value.endHour,
                        sessionId = if (_state.value.sessionId == 0L) state.value.sessionId else _state.value.sessionId,
                        location = if (_state.value.location.isBlank()) state.value.location else _state.value.location,
                        conversation = if (!_state.value.conversation) state.value.conversation else _state.value.conversation,
                        contact = if (!_state.value.contact) state.value.contact else _state.value.contact,
                        instantDate = if (!_state.value.instantDate) state.value.instantDate else _state.value.instantDate,
                        recorded = if (!_state.value.recorded) state.value.recorded else _state.value.recorded,
                        leadId = if (_state.value.leadId == 0L) state.value.leadId else _state.value.leadId,
                        dateId = dateId,
                        stickingPoints = if (_state.value.stickingPoints.isBlank()) state.value.stickingPoints else _state.value.stickingPoints,
                        tweetUrl = if (_state.value.tweetUrl.isBlank()) state.value.tweetUrl else _state.value.tweetUrl
                    )
                    var leadId = 0L
                    if (isAddingSet) {
                        if (!state.value.leads.isEmpty()) {
                            val lead = state.value.leads[0]
                            lead.insertTime = set.insertTime
                            leadId = leadDao.insert(lead)
                        }
                        if (leadId != 0L) {
                            set.leadId = leadId
                        }
                    } else if (isUpdatingSet) {
                        set.id = state.value.editSet!!.id
                        set.insertTime = state.value.editSet!!.insertTime
                    }
                    setDao.insert(set)
                    if (isAddingSet && !isUpdatingSet && _state.value.generateiDate && _state.value.instantDate) {
                        val date = _dateService.init(
                            id = dateId.toString(),
                            leadId = leadId,
                            location = if (_state.value.location.isBlank()) state.value.location else _state.value.location,
                            date = if (_state.value.date.isBlank()) state.value.date else _state.value.date,
                            startHour = if (_state.value.startHour.isBlank()) state.value.startHour else _state.value.startHour,
                            endHour = if (_state.value.endHour.isBlank()) state.value.endHour else _state.value.endHour,
                            cost = 0,
                            dateNumber = 0,
                            dateType = DateType.DRINK.getType(),
                            pull = false,
                            bounce = false,
                            kiss = false,
                            lay = false,
                            recorded = if (!_state.value.recorded) state.value.recorded else _state.value.recorded,
                            stickingPoints = if (_state.value.stickingPoints.isBlank()) state.value.stickingPoints else _state.value.stickingPoints,
                            tweetUrl = if (_state.value.tweetUrl.isBlank()) state.value.tweetUrl else _state.value.tweetUrl
                        )
                        dateDao.insert(date)
                    }
                    _state.update {
                        it.copy(
                            date = "",
                            startHour = "",
                            endHour = "",
                            sessionId = 0L,
                            location = "",
                            conversation = false,
                            contact = false,
                            instantDate = false,
                            recorded = false,
                            leadId = 0L,
                            dateId = 0L,
                            stickingPoints = "",
                            tweetUrl = "",
                            setSortType = SetSortType.DATE,
                            isAddingSet = false,
                            isUpdatingSet = false
                        )
                    }
                }
            }

            is GameEvent.SetSessionId -> {
                _state.update {
                    it.copy(
                        sessionId = event.sessionId
                    )
                }
            }

            is GameEvent.SetDateId -> {
                _state.update {
                    it.copy(
                        dateId = event.dateId
                    )
                }
            }

            is GameEvent.SwitchConversation -> {
                _state.update {
                    it.copy(
                        conversation = _state.value.conversation.not()
                    )
                }
            }

            is GameEvent.SwitchContact -> {
                _state.update {
                    it.copy(
                        contact = _state.value.contact.not()
                    )
                }
            }

            is GameEvent.SwitchInstantDate -> {
                _state.update {
                    it.copy(
                        instantDate = _state.value.instantDate.not()
                    )
                }
            }

            is GameEvent.SortSets -> {
                _setSortType.value = event.sortType
            }

            is GameEvent.SortGameEvents -> {
                _gameEventSortType.value = event.gameEventSortType
            }

            is GameEvent.DeleteSet -> {
                viewModelScope.launch {
                    setDao.delete(event.set)
                }
            }

            is GameEvent.EditSet -> {
                _state.update {
                    it.copy(
                        editSet = event.set
                    )
                }
            }

            is GameEvent.SetConversation -> {
                _state.update {
                    it.copy(
                        conversation = event.conversation!!
                    )
                }
            }

            is GameEvent.SetContact -> {
                _state.update {
                    it.copy(
                        contact = event.contact!!
                    )
                }
            }

            is GameEvent.SetInstantDate -> {
                _state.update {
                    it.copy(
                        instantDate = event.instantDate!!
                    )
                }
            }
        }
    }
}
