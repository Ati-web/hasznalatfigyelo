package com.appusagetracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.appusagetracker.data.AppUsageDatabase
import com.appusagetracker.data.AppUsageEntity
import com.appusagetracker.util.DateHelper
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = AppUsageDatabase.getDatabase(application)
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _dateRange = MutableLiveData<Pair<Long, Long>>()
    
    val appUsageList: LiveData<List<AppUsageEntity>> = 
        _dateRange.switchMap { (startDate, endDate) ->
            database.appUsageDao().getUsageByDateRange(startDate, endDate).asLiveData()
        }
    
    init {
        // Alapértelmezett: ma
        val todayStart = DateHelper.getTodayStart()
        val tomorrowStart = DateHelper.getTomorrowStart()
        _dateRange.value = Pair(todayStart, tomorrowStart)
    }
    
    fun loadUsageData(startDate: Long, endDate: Long) {
        _isLoading.value = true
        _dateRange.value = Pair(startDate, endDate)
        _isLoading.value = false
    }
    
    fun deleteAllData() {
        viewModelScope.launch {
            database.appUsageDao().deleteAllUsage()
        }
    }
}

