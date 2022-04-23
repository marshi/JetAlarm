package dev.marshi.jetalarm.data

import dev.marshi.jetalarm.ui.model.Alarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val dao: AlarmDao
) : AlarmRepository {

    override suspend fun insert(alarm: Alarm) = withContext(Dispatchers.IO) {
        val entity = alarm.toEntity()
        dao.insert(entity = entity)
    }

    override suspend fun update(alarm: Alarm) = withContext(Dispatchers.IO) {
        val entity = dao.find(alarm.id)?.copy(
            hour = alarm.hour,
            minute = alarm.minute
        ) ?: return@withContext
        dao.update(entity)
    }

    override fun list(): Flow<List<Alarm>> {
        return dao.list().map {
            it.map { entity -> Alarm.from(entity) }
        }.flowOn(Dispatchers.IO)
    }

    override fun find(id: Long): Alarm? {
        return dao.find(id)?.let { entity ->
            Alarm.from(entity)
        }
    }

    override suspend fun remove(alarm: Alarm) = withContext(Dispatchers.IO) {
        dao.delete(
            AlarmEntity(
                id = alarm.id,
                insertedAt = 0L,
                updatedAt = 0L,
            )
        )
    }
}
