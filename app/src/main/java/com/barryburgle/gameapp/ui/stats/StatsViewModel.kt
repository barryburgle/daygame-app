package com.barryburgle.gameapp.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.ui.CombineEight
import com.barryburgle.gameapp.ui.stats.state.StatsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val leadDao: LeadDao
) : ViewModel() {
    private val _state =
        MutableStateFlow(StatsState())
    private val _abstractSessions = abstractSessionDao.getAll()
    private val _leads = leadDao.getAll()
    private val _setsHistogram = abstractSessionDao.getSetsHistogram()
    private val _convosHistogram = abstractSessionDao.getConvosHistogram()
    private val _contactsHistogram = abstractSessionDao.getContactsHistogram()
    private val _ageHistogram = leadDao.getAgeHistogram()
    private val _nationalityHistogram = leadDao.getNationalityHistogram()

    val state =
        CombineEight(
            _state,
            _abstractSessions,
            _leads,
            _setsHistogram,
            _convosHistogram,
            _contactsHistogram,
            _ageHistogram,
            _nationalityHistogram
        ) { state, abstractSessions, leads, setsHistogram, convosHistogram, contactsHistogram, ageHistogram, nationalityHistogram ->
            state.copy(
                abstractSessions = abstractSessions,
                leads = leads,
                setsHistogram = setsHistogram,
                convosHistogram = convosHistogram,
                contactsHistogram = contactsHistogram,
                ageHistogram = ageHistogram,
                nationalityHistogram = nationalityHistogram
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsState())
}