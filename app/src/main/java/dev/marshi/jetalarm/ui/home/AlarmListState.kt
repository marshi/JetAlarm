package dev.marshi.jetalarm.ui.home

import dev.marshi.jetalarm.ui.model.Alarm
import java.util.*

data class AlarmListState(
    val alarms: List<Alarm> = emptyList(),
    val scroll: List<Event.Scroll> = emptyList(),
)

sealed class Event {
    data class Scroll(
        val id: Long = UUID.randomUUID().mostSignificantBits
    ) : Event()
}
