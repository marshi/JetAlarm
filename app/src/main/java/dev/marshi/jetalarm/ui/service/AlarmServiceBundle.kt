package dev.marshi.jetalarm.ui.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlarmServiceBundle(
    val operation: AlarmOperation,
) : Parcelable

enum class AlarmOperation {
    Start,
    Stop,
}