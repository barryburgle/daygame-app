package com.barryburgle.gameapp.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.challenge.ChallengeDao
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.set.SetDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.StatsEvent
import com.barryburgle.gameapp.model.enums.StatsLoadInfoEnum
import com.barryburgle.gameapp.ui.CombineSixteen
import com.barryburgle.gameapp.ui.stats.state.StatsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class StatsViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val leadDao: LeadDao,
    private val dateDao: DateDao,
    private val challengeDao: ChallengeDao,
    private val setDao: SetDao,
    private val settingDao: SettingDao,
) : ViewModel() {
    private val _state =
        MutableStateFlow(StatsState())
    private val _allSessions = abstractSessionDao.getAll()
    private val _allLeads = leadDao.getAll()
    private val _allDates = dateDao.getAll()
    private val _allChallenges = challengeDao.getAll()
    private val _allSets = setDao.getAll()
    private val _setsHistogram = abstractSessionDao.getSetsHistogram()
    private val _convosHistogram = abstractSessionDao.getConvosHistogram()
    private val _contactsHistogram = abstractSessionDao.getContactsHistogram()
    private val _leadsAgeHistogram = leadDao.getAgeHistogram()
    private val _leadsNationalityHistogram = leadDao.getNationalityHistogram()
    private val _datesAgeHistogram = dateDao.getAgeHistogram()
    private val _datesNumberHistogram = dateDao.getNumberHistogram()
    private val _datesNationalityHistogram = dateDao.getNationalityHistogram()
    private val _loadInfoType = MutableStateFlow(StatsLoadInfoEnum.SESSION_SETS)
    private val _completeHistogram = _loadInfoType.flatMapLatest { loadInfo ->
        when (loadInfo) {
            StatsLoadInfoEnum.SESSION_SETS -> abstractSessionDao.getSetsHistogram()
            StatsLoadInfoEnum.SESSION_CONVOS -> abstractSessionDao.getConvosHistogram()
            StatsLoadInfoEnum.SESSION_CONTACTS -> abstractSessionDao.getContactsHistogram()
            StatsLoadInfoEnum.LEAD_AGES -> leadDao.getAgeHistogram()
            StatsLoadInfoEnum.LEAD_COUNTRIES -> leadDao.getAllNationalityHistogram()
            StatsLoadInfoEnum.DATE_AGES -> dateDao.getAgeHistogram()
            StatsLoadInfoEnum.DATE_NUMBER -> dateDao.getNumberHistogram()
            StatsLoadInfoEnum.DATE_COUNTRIES -> dateDao.getAllNationalityHistogram()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _copyReportOnClipboard = settingDao.getCopyReportOnClipboard()

    val state =
        CombineSixteen(
            _state,
            _allSessions,
            _allLeads,
            _allDates,
            _allChallenges,
            _allSets,
            _setsHistogram,
            _convosHistogram,
            _contactsHistogram,
            _leadsAgeHistogram,
            _leadsNationalityHistogram,
            _datesAgeHistogram,
            _datesNumberHistogram,
            _datesNationalityHistogram,
            _completeHistogram,
            _copyReportOnClipboard
        ) { state, allSessions, allLeads, allDates, allChallenges, allSets, setsHistogram, convosHistogram, contactsHistogram, leadsAgeHistogram, leadsNationalityHistogram, datesAgeHistogram, datesNumberHistogram, datesNationalityHistogram, completeHistogram, copyReportOnClipboard ->
            state.copy(
                allSessions = allSessions,
                allLeads = allLeads,
                allDates = allDates,
                allChallenges = allChallenges,
                allSets = allSets,
                setsHistogram = setsHistogram,
                convosHistogram = convosHistogram,
                contactsHistogram = contactsHistogram,
                leadsAgeHistogram = leadsAgeHistogram,
                leadsNationalityHistogram = leadsNationalityHistogram,
                datesAgeHistogram = datesAgeHistogram,
                datesNumberHistogram = datesNumberHistogram,
                datesNationalityHistogram = datesNationalityHistogram,
                completeHistogram = completeHistogram,
                copyReportOnClipboard = copyReportOnClipboard.toBoolean()
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsState())

    fun onEvent(event: StatsEvent) {
        when (event) {
            is StatsEvent.ShowInfo -> {
                _loadInfoType.value = event.statsLoadInfo
                _state.update {
                    it.copy(
                        infoDialogTitle = event.statsLoadInfo.getTracker(),
                        trackedEntity = event.statsLoadInfo.getTrackedEntity(),
                        completeHistogram = _state.value.completeHistogram,
                        isShowingInfo = true
                    )
                }
            }

            is StatsEvent.HideInfo -> {
                _state.update {
                    it.copy(
                        isShowingInfo = false
                    )
                }
            }
        }
    }
}