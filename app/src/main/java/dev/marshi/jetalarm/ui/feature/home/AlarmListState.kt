package dev.marshi.jetalarm.ui.feature.home

import dev.marshi.jetalarm.ui.model.Alarm

data class AlarmListState(
    val alarms: List<Alarm> = emptyList(),
    val isExpandedSet: Set<Int> = emptySet(),
) {

}
