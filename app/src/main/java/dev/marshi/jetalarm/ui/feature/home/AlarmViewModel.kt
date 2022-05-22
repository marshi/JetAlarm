package dev.marshi.jetalarm.ui.feature.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.marshi.jetalarm.data.AlarmRepository
import dev.marshi.jetalarm.domain.model.Alarm
import dev.marshi.jetalarm.domain.model.NoIdAlarm
import dev.marshi.jetalarm.ui.util.JetAlarmManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmListState())
    val state = _state.asStateFlow()

    init {
        alarmRepository.list().onEach { alarms ->
            _state.update { it.copy(alarms = alarms) }
        }.launchIn(viewModelScope)
    }

    fun remove(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.remove(alarm)
            JetAlarmManager.deleteAlarm(context, alarm.id)
        }
    }

    fun add(alarm: NoIdAlarm) {
        viewModelScope.launch {
            alarmRepository.insert(alarm)
        }
    }

    fun updateDayOfWeek(id: Int, dayOfWeek: DayOfWeek, active: Boolean) {
        viewModelScope.launch {
            val alarm = alarmRepository.find(id) ?: return@launch
            val newDayOfWeek = if (active) {
                alarm.dayOfWeeks + dayOfWeek
            } else {
                alarm.dayOfWeeks - dayOfWeek
            }
            val updateAlarm = alarm.copy(dayOfWeeks = newDayOfWeek)
            alarmRepository.update(updateAlarm)
        }
    }

    fun updateActive(id: Int, isActive: Boolean) {
        viewModelScope.launch {
            val alarm = alarmRepository.find(id)?.copy(isActive = isActive) ?: return@launch
            alarmRepository.update(alarm)
        }
    }

    fun update(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.update(alarm)
        }
    }
}
