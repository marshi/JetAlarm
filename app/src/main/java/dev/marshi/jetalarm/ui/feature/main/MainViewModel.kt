package dev.marshi.jetalarm.ui.feature.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.marshi.jetalarm.data.AlarmRepository
import dev.marshi.jetalarm.domain.model.Alarm
import dev.marshi.jetalarm.ui.util.JetAlarmManager
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    fun addAlarm(hour: Int, minute: Int) {
        viewModelScope.launch {
            val alarm = Alarm(hour = hour, minute = minute, isActive = true)
            alarmRepository.insert(alarm)
            JetAlarmManager.setAlarm(context, alarm)
        }
    }
}
