package com.example.and101_capstone.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "History of Completed Tasks Stretch Feature Later"
    }
    val text: LiveData<String> = _text
}