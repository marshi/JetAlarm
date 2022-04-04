package dev.marshi.jetalarm.ui.model

import java.util.*

data class Alarm(
    val id: String = UUID.randomUUID().toString(),
    val time: String,
)