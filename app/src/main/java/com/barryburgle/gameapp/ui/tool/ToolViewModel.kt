package com.barryburgle.gameapp.ui.tool

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToolViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is tool Fragment"
    }
    val text: LiveData<String> = _text
}