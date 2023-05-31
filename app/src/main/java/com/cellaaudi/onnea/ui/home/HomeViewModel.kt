package com.cellaaudi.onnea.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class HomeViewModel : ViewModel() {

    private val _date = MutableLiveData<Date>()
    val date: LiveData<Date> = _date

    fun setDate(selDate: Date) {
        _date.value = selDate
    }
}