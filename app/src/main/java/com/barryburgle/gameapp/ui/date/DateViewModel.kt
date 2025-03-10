package com.barryburgle.gameapp.ui.date

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.event.DateEvent
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.ui.CombineThree
import com.barryburgle.gameapp.ui.date.state.DateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class DateViewModel(
    private val dateDao: DateDao,
    private val leadDao: LeadDao
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
    private val _allLeads = leadDao.getAll()

    val state =
        CombineThree(
            _state,
            _dates,
            _allLeads
        ) { state, dates, allLeads ->
            state.copy(
                dates = dates,
                allLeads = allLeads
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DateState())

    fun onEvent(event: DateEvent) {
        when (event) {
            is DateEvent.SaveDate -> { /*TODO: write implementation*/
            }

            is DateEvent.ShowDialog -> { /*TODO: write implementation*/
            }

            is DateEvent.HideDialog -> { /*TODO: write implementation*/
            }

            is DateEvent.SetLeadId -> { /*TODO: write implementation*/
            }

            is DateEvent.SetLocation -> { /*TODO: write implementation*/
            }

            is DateEvent.SetMeetingDate -> { /*TODO: write implementation*/
            }

            is DateEvent.SetStartTime -> { /*TODO: write implementation*/
            }

            is DateEvent.SetEndTime -> { /*TODO: write implementation*/
            }

            is DateEvent.SetCost -> { /*TODO: write implementation*/
            }

            is DateEvent.SetDateNumber -> { /*TODO: write implementation*/
            }

            is DateEvent.SetPull -> { /*TODO: write implementation*/
            }

            is DateEvent.SetBounce -> { /*TODO: write implementation*/
            }

            is DateEvent.SetKiss -> { /*TODO: write implementation*/
            }

            is DateEvent.SetLay -> { /*TODO: write implementation*/
            }

            is DateEvent.SetRecorded -> { /*TODO: write implementation*/
            }

            is DateEvent.SetStickingPoints -> { /*TODO: write implementation*/
            }

            is DateEvent.SetTweetUrl -> { /*TODO: write implementation*/
            }

            is DateEvent.SortDates -> { /*TODO: write implementation*/
            }

            is DateEvent.DeleteDate -> { /*TODO: write implementation*/
            }

            is DateEvent.EditDate -> { /*TODO: write implementation*/
            }

            is DateEvent.EditLead -> { /*TODO: write implementation*/
            }
        }
    }
}