package dev.marshi.jetalarm.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.marshi.jetalarm.data.AlarmRepository
import dev.marshi.jetalarm.ui.util.JetAlarmManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class JetAlarmAppViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    fun observeAndUpdateLatestAlarm() {
        alarmRepository.latest().onEach { alarm ->
            alarm ?: return@onEach
            if (alarm.isActive) {
                JetAlarmManager.setAlarm(context, alarm)
            } else {
                JetAlarmManager.deleteAlarm(context, alarm)
            }
        }.launchIn(viewModelScope)
    }
}