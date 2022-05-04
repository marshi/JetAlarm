package dev.marshi.jetalarm.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.marshi.jetalarm.data.AlarmRepository
import dev.marshi.jetalarm.ui.model.Alarm
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
        }
    }

    fun add(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.insert(alarm)
        }
    }

    fun updateDayOfWeek(id: Long, dayOfWeek: DayOfWeek, active: Boolean) {
        viewModelScope.launch {
            val alarm = alarmRepository.find(id) ?: return@launch
            val newDayOfWeek = if (active) {
                alarm.dayOfWeek + dayOfWeek
            } else {
                alarm.dayOfWeek - dayOfWeek
            }
            val updateAlarm = alarm.copy(dayOfWeek = newDayOfWeek)
            alarmRepository.update(updateAlarm)
        }
    }

    fun updateActive(id: Long, isActive: Boolean) {
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
