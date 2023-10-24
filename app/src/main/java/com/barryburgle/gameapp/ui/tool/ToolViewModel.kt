package com.barryburgle.gameapp.ui.tool

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import kotlinx.coroutines.flow.MutableStateFlow

class ToolViewModel(private val abstractSessionDao: AbstractSessionDao) : ViewModel() {
    private val _state = MutableStateFlow(ToolsState())
    val state = _state
    private val _text = MutableLiveData<String>().apply {
        value = "This is tool Fragment"
    }
    val text: LiveData<String> = _text
}