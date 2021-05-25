package com.example.tracksleep.sleepquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tracksleep.database.SleepDatabaseDao
import com.example.tracksleep.sleeptracker.SleepTrackerViewModel
import java.lang.IllegalArgumentException

class SleepQualityViewModelFactory(
        private val sleepNightKey : Long = 0L,
        private val dataSource : SleepDatabaseDao
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepQualityViewModel::class.java)){
            return SleepQualityViewModel(sleepNightKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}