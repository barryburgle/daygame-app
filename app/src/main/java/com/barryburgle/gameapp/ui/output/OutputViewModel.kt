package com.barryburgle.gameapp.ui.output

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OutputViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is output Fragment"
    }
    val text: LiveData<String> = _text
}