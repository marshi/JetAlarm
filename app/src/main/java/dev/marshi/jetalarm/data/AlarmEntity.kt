package dev.marshi.jetalarm.data

import androidx.annotation.IntRange
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.time.LocalTime

@Entity
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "hour") val hour: Int = 0,
    @ColumnInfo(name = "minute") val minute: Int = 0,
    @ColumnInfo(name = "inserted_at") val insertedAt: Long = 0L,
    @ColumnInfo(name = "udpated_at") val updatedAt: Long = 0L,
    @ColumnInfo(name = "day_of_week") @IntRange(from = 0, to = 127) val dayOfWeek: Int = 0,
    @ColumnInfo(name = "active") val active: Boolean = false,
) {

    companion object {
        fun nowTimestamp(): Timestamp {
            val now = LocalTime.now().second * 1000L
            return Timestamp(now)
        }
    }
}
