package dev.marshi.jetalarm.ui.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.marshi.jetalarm.data.AlarmRepository
import dev.marshi.jetalarm.domain.model.NoIdAlarm
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    fun addAlarm(hour: Int, minute: Int) {
        viewModelScope.launch {
            val noIdAlarm = NoIdAlarm(hour = hour, minute = minute, isActive = true)
            alarmRepository.insert(noIdAlarm)
        }
    }
}
