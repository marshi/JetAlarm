package dev.marshi.jetalarm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.marshi.jetalarm.ui.model.Alarm
import java.sql.Timestamp
import java.time.LocalTime
import java.util.*

@Entity
data class AlarmEntity(
    @PrimaryKey val id: Long = UUID.randomUUID().mostSignificantBits,
    @ColumnInfo(name = "hour") val hour: Int = 0,
    @ColumnInfo(name = "minute") val minute: Int = 0,
    @ColumnInfo(name = "inserted_at") val insertedAt: Long = 0L,
    @ColumnInfo(name = "udpated_at") val updatedAt: Long = 0L,
) {

    companion object {
        fun nowTimestamp(): Timestamp {
            val now = LocalTime.now().second * 1000L
            return Timestamp(now)
        }
    }
}
