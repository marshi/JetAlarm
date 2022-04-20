package dev.marshi.jetalarm.ui.home

import dev.marshi.jetalarm.ui.model.Alarm

data class AlarmListState(
    val alarms: List<Alarm> = emptyList(),
)
