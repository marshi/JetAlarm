package dev.marshi.jetalarm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.marshi.jetalarm.data.AlarmRepository
import dev.marshi.jetalarm.ui.model.Alarm
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    fun addAlarm(hour: Int, minute: Int) {
        viewModelScope.launch {
            alarmRepository.insert(Alarm(hour = hour, minute = minute))
        }
    }
}
