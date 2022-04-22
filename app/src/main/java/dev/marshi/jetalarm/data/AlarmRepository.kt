package dev.marshi.jetalarm.data

import dev.marshi.jetalarm.ui.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun insert(entity: Alarm)

    suspend fun update(entity: AlarmEntity)

    suspend fun remove(alarm: Alarm)

    fun list(): Flow<List<Alarm>>
}
