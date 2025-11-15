package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {

    // LiveData
    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    init {
        // Nilai awal
        _total.postValue(0)
    }

    fun incrementTotal() {
        _total.postValue((_total.value ?: 0) + 1)
    }
}
