package com.barryburgle.gameapp.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.set.SetDao
import com.barryburgle.gameapp.ui.CombineThirteen
import com.barryburgle.gameapp.ui.stats.state.StatsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val leadDao: LeadDao,
    private val dateDao: DateDao,
    private val setDao: SetDao
) : ViewModel() {
    private val _state =
        MutableStateFlow(StatsState())
    private val _allSessions = abstractSessionDao.getAll()
    private val _allLeads = leadDao.getAll()
    private val _allDates = dateDao.getAll()
    private val _allSets = setDao.getAll()
    private val _setsHistogram = abstractSessionDao.getSetsHistogram()
    private val _convosHistogram = abstractSessionDao.getConvosHistogram()
    private val _contactsHistogram = abstractSessionDao.getContactsHistogram()
    private val _leadsAgeHistogram = leadDao.getAgeHistogram()
    private val _leadsNationalityHistogram = leadDao.getNationalityHistogram()
    private val _datesAgeHistogram = dateDao.getAgeHistogram()
    private val _datesNumberHistogram = dateDao.getNumberHistogram()
    private val _datesNationalityHistogram = dateDao.getNationalityHistogram()

    val state =
        CombineThirteen(
            _state,
            _allSessions,
            _allLeads,
            _allDates,
            _allSets,
            _setsHistogram,
            _convosHistogram,
            _contactsHistogram,
            _leadsAgeHistogram,
            _leadsNationalityHistogram,
            _datesAgeHistogram,
            _datesNumberHistogram,
            _datesNationalityHistogram
        ) { state, allSessions, allLeads, allDates, allSets, setsHistogram, convosHistogram, contactsHistogram, leadsAgeHistogram, leadsNationalityHistogram, datesAgeHistogram, datesNumberHistogram, datesNationalityHistogram ->
            state.copy(
                allSessions = allSessions,
                allLeads = allLeads,
                allDates = allDates,
                allSets = allSets,
                setsHistogram = setsHistogram,
                convosHistogram = convosHistogram,
                contactsHistogram = contactsHistogram,
                leadsAgeHistogram = leadsAgeHistogram,
                leadsNationalityHistogram = leadsNationalityHistogram,
                datesAgeHistogram = datesAgeHistogram,
                datesNumberHistogram = datesNumberHistogram,
                datesNationalityHistogram = datesNationalityHistogram
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsState())
}