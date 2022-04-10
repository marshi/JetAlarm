package dev.marshi.jetalarm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val dao: AlarmDao
) : AlarmRepository {
    override suspend fun insert(entity: AlarmEntity) = withContext(Dispatchers.IO) {
        dao.insert(entity = entity)
    }

    override suspend fun update(entity: AlarmEntity) = withContext(Dispatchers.IO) {
        dao.update(entity)
    }

    override suspend fun list(): List<AlarmEntity> = withContext(Dispatchers.IO) {
        dao.list()
    }
}
