package dev.marshi.jetalarm.data

import dev.marshi.jetalarm.domain.model.Alarm
import dev.marshi.jetalarm.domain.model.NoIdAlarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun insert(noIdAlarm: NoIdAlarm): Int

    suspend fun update(alarm: Alarm)

    suspend fun remove(alarm: Alarm)

    suspend fun find(id: Int): Alarm?

    fun list(): Flow<List<Alarm>>

}
