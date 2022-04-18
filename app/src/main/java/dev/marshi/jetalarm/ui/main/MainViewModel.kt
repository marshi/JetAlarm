package dev.marshi.jetalarm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.marshi.jetalarm.data.AlarmRepository
import dev.marshi.jetalarm.ui.model.Alarm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    val state = MutableStateFlow(MainState())

    fun addAlarm() {
        viewModelScope.launch {
            state.update {
                it.copy(
                    addAlarms = it.addAlarms + AddAlarm(
                        alarm = Alarm.now()
                    )
                )
            }
        }
    }

    fun added(alarmId: Long) {
        val addAlarms = state.value.addAlarms.filterNot { it.id == alarmId }
        state.update {
            it.copy(addAlarms = addAlarms)
        }
    }
}
