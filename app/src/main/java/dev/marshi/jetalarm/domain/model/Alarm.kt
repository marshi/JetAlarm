package dev.marshi.jetalarm.domain.model

import android.icu.util.Calendar
import dev.marshi.jetalarm.data.AlarmEntity
import dev.marshi.jetalarm.extensions.dayOfWeekFrom
import dev.marshi.jetalarm.extensions.toNumeric
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

data class Alarm(
    val id: String = UUID.randomUUID().toString(),
    val hour: Int = 0,
    val minute: Int = 0,
    val dayOfWeeks: Set<DayOfWeek> = emptySet(),
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
                dayOfWeeks = dayOfWeekFrom(entity.dayOfWeek),
                isActive = entity.active,
            )
        }
    }

    private val localTime = LocalTime.of(hour, minute)

    val timeStr: String = localTime.format(formatter)

    val timeInMillis: Long
        get() {
            val cal = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, this@Alarm.hour)
                set(Calendar.MINUTE, this@Alarm.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            return cal.timeInMillis
        }

    val nextDayTimeInMillis: Long
        get() {
            val cal = Calendar.getInstance().apply {
                this.timeInMillis = this@Alarm.timeInMillis
                add(Calendar.DAY_OF_MONTH, 1)
            }
            return cal.timeInMillis
        }

    fun enabledOn(dayOfWeek: DayOfWeek): Boolean {
        return dayOfWeeks.contains(dayOfWeek)
    }

    fun toEntity(): AlarmEntity {
        val now = Date().time
        return AlarmEntity(
            id = id,
            hour = hour,
            minute = minute,
            insertedAt = now,
            updatedAt = now,
            dayOfWeek = dayOfWeeks.toNumeric(),
            active = isActive
        )
    }
}