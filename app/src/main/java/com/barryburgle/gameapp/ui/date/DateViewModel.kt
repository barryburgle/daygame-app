package com.barryburgle.gameapp.ui.date

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.event.DateEvent
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.ui.CombineTwo
import com.barryburgle.gameapp.ui.date.state.DateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class DateViewModel(
    private val dateDao: DateDao
) : ViewModel() {
    private val _state =
        MutableStateFlow(DateState())
    private val _dateSortType = MutableStateFlow(DateSortType.DATE)
    private val _dates = _dateSortType.flatMapLatest { sortType ->
        when (sortType) {
            DateSortType.DATE -> dateDao.getByDate()
            DateSortType.LEAD -> dateDao.getByLead()
            DateSortType.LOCATION -> dateDao.getByLocation()
            DateSortType.START_TIME -> dateDao.getByStartTime()
            DateSortType.END_TIME -> dateDao.getByEndTime()
            DateSortType.DATE_TIME -> dateDao.getByDate() /*TODO: COMPUTE DATE TIME ON THE FLY AND SORT IN LIST*/
            DateSortType.COST -> dateDao.getByCost()
            DateSortType.DATE_NUMBER -> dateDao.getByDateNumber()
            DateSortType.PULL -> dateDao.getPulled()
            DateSortType.NOT_PULL -> dateDao.getNotPulled()
            DateSortType.BOUNCE -> dateDao.getBounced()
            DateSortType.NOT_BOUNCE -> dateDao.getNotBounced()
            DateSortType.KISS -> dateDao.getKissed()
            DateSortType.NOT_KISS -> dateDao.getNotKissed()
            DateSortType.LAY -> dateDao.getLaid()
            DateSortType.NOT_LAY -> dateDao.getNotLaid()
            DateSortType.RECORDED -> dateDao.getRecorded()
            DateSortType.NOT_RECORDED -> dateDao.getNotRecorded()
            DateSortType.DAY_OF_WEEK -> dateDao.getByDate()/*TODO: COMPUTE ON THE FLY AND SORT IN LIST*/
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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