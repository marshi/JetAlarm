package dev.marshi.jetalarm.ui.model

import dev.marshi.jetalarm.data.AlarmEntity
import dev.marshi.jetalarm.extensions.dayOfWeekFrom
import dev.marshi.jetalarm.extensions.toNumeric
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

data class Alarm(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    val hour: Int = 0,
    val minute: Int = 0,
    val dayOfWeek: Set<DayOfWeek> = emptySet(),
    val isActive: Boolean = false,
) {
    companion object {
        private val formatter = DateTimeFormatter.ofPattern("HH:mm")

        fun now(): Alarm {
            val now = LocalTime.now()
            return Alarm(hour = now.hour, minute = now.minute)
        }

        fun from(entity: AlarmEntity): Alarm {
            return Alarm(
                id = entity.id,
                hour = entity.hour,
                minute = entity.minute,
                dayOfWeek = dayOfWeekFrom(entity.dayOfWeek),
                isActive = entity.active,
            )
        }
    }

    private val localTime = LocalTime.of(hour, minute)

    val timeStr: String = localTime.format(formatter)

    fun toEntity(): AlarmEntity {
        val now = Date().time
        return AlarmEntity(
            id = id,
            hour = hour,
            minute = minute,
            insertedAt = now,
            updatedAt = now,
            dayOfWeek = dayOfWeek.toNumeric(),
            active = isActive
        )
    }
}