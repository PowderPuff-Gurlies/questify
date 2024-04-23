package com.example.and101_capstone.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "CalendarView Stretch Feature Later"
    }
    val text: LiveData<String> = _text
}