package com.example.tracksleep.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracksleep.database.SleepDatabaseDao
import kotlinx.coroutines.launch

class SleepQualityViewModel(
        private val sleepNightKey : Long = 0L,
        val database: SleepDatabaseDao
) : ViewModel() {
    private val _navigateToSleepTrackerFragment = MutableLiveData<Boolean?>()
    val navigateToSleepTracker : LiveData<Boolean?>
        get() = _navigateToSleepTrackerFragment

    fun doneNavigating(){
        _navigateToSleepTrackerFragment.value = null
    }

    fun onSetSleepQuality(quality : Int){
        viewModelScope.launch {
            val tonight = database.getTonight() ?: return@launch
            tonight.sleepQuality = quality
            database.update(tonight)

            _navigateToSleepTrackerFragment.value = true
        }
    }
}