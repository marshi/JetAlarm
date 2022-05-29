package dev.marshi.jetalarm.ui.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class AlarmReceiverBundle() : Parcelable {
    @Parcelize
    data class StartAlarm(
        val id: Int,
    ) : AlarmReceiverBundle()

    @Parcelize
    object StopAlarm : AlarmReceiverBundle()
}