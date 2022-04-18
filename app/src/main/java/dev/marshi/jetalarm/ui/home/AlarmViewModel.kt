package dev.marshi.jetalarm.ui.home

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

    fun scrollToTop() {
        _state.update {
            it.copy(scroll = it.scroll + Event.Scroll())
        }
    }

    fun scrolled(id: Long) {
        val scrolls = state.value.scroll.filterNot { it.id == id }
        _state.update {
            it.copy(scroll = scrolls)
        }
    }

    fun add(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.insert(alarm)
        }
    }
}
