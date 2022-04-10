package dev.marshi.jetalarm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "time") val time: Long
)
