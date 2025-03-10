package com.barryburgle.gameapp.ui.date

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.ui.CombineTwo
import com.barryburgle.gameapp.ui.date.state.DateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DateViewModel(
    private val dateDao: DateDao
) : ViewModel() {
    private val _state =
        MutableStateFlow(DateState())
    private val _dates = dateDao.getAll()

    val state =
        CombineTwo(
            _state,
            _dates
        ) { state, dates ->
            state.copy(
                dates = dates
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DateState())
}