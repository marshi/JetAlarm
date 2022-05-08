package dev.marshi.jetalarm.data

import dev.marshi.jetalarm.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun insert(alarm: Alarm)

    suspend fun update(alarm: Alarm)

    suspend fun remove(alarm: Alarm)

    suspend fun find(id: String): Alarm?

    fun list(): Flow<List<Alarm>>

}
