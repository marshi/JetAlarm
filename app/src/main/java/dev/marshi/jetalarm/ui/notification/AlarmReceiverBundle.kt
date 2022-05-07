package dev.marshi.jetalarm.ui.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlarmReceiverBundle(
    val id: String,
) : Parcelable