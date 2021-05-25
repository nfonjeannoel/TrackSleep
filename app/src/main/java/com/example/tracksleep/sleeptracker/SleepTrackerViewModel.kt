package com.example.tracksleep.sleeptracker

import android.app.Application
import androidx.lifecycle.*
import com.example.tracksleep.database.SleepDatabaseDao
import com.example.tracksleep.database.SleepNight
import com.example.tracksleep.formatNights
import kotlinx.coroutines.launch

class SleepTrackerViewModel(
    val database : SleepDatabaseDao,
    application: Application
)  : AndroidViewModel(application){


    private var tonight = MutableLiveData<SleepNight?>()
    val nights = database.getAllNights()

    private var _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality : LiveData<SleepNight>
        get() = _navigateToSleepQuality

    private var _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent : LiveData<Boolean>
        get() = _showSnackBarEvent

    fun doneShowingSnackBar(){
        _showSnackBarEvent.value = false
    }

    val nightsString = Transformations.map(nights){ nights ->
        formatNights(nights, application.resources)
    }
    init {
        initializeTonight()
    }

    val startButtonVisible = Transformations.map(tonight){
        it == null
    }
    val stopButtonVisible = Transformations.map(tonight){
        it != null
    }
    val clearButtonVisible = Transformations.map(nights){
        it?.isNotEmpty()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        var night = database.getTonight()
        if (night?.endTimeMilli != night?.startTimeMilli){
            night = null
        }
        return night
    }

    fun onStartTracking(){
        viewModelScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }
    private suspend fun insert(night: SleepNight){
        database.insert(night)
    }

    fun onStopTracking(){
        viewModelScope.launch {
            var oldNight = tonight.value?:return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night: SleepNight){
        database.update(night)
    }
    fun onClear(){
        viewModelScope.launch {
            clear()
            tonight.value = null
            _showSnackBarEvent.value = true
        }
    }
    private suspend fun clear(){
        database.clear()
    }

    fun doneNavigating(){
        _navigateToSleepQuality.value = null
    }
}