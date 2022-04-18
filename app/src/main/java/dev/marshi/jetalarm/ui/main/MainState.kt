package dev.marshi.jetalarm.ui.main

import dev.marshi.jetalarm.ui.model.Alarm
import java.util.*

data class MainState(
   val addAlarms: List<AddAlarm> = emptyList(),
)

data class AddAlarm(
   val id: Long = UUID.randomUUID().mostSignificantBits,
   val alarm: Alarm,
)
