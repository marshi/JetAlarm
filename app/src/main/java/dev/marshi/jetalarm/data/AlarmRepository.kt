package dev.marshi.jetalarm.data

interface AlarmRepository {
    suspend fun insert(entity: AlarmEntity)

    suspend fun update(entity: AlarmEntity)

    suspend fun list(): List<AlarmEntity>
}