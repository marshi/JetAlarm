package dev.marshi.jetalarm.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.marshi.jetalarm.domain.model.Alarm
import dev.marshi.jetalarm.domain.model.NoIdAlarm
import dev.marshi.jetalarm.extensions.toNumeric
import dev.marshi.jetalarm.ui.util.JetAlarmManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: AlarmDao
) : AlarmRepository {

    override suspend fun insert(alarm: NoIdAlarm): Int = withContext(Dispatchers.IO) {
        val entity = alarm.toNewEntity()
        dao.insert(entity = entity).toInt()
    }

    override suspend fun update(alarm: Alarm) = withContext(Dispatchers.IO) {
        val entity = dao.find(alarm.id)?.copy(
            hour = alarm.hour,
            minute = alarm.minute,
            dayOfWeek = alarm.dayOfWeeks.toNumeric(),
            active = alarm.isActive,
            updatedAt = Date().time,
        ) ?: return@withContext
        dao.update(entity)
    }

    override fun list(): Flow<List<Alarm>> {
        return dao.list().map {
            it.map { entity -> Alarm.from(entity) }
        }.flowOn(Dispatchers.IO)
    }

    override fun latest(): Flow<Alarm?> {
        return dao.latest().map { entity ->
            entity?.let { Alarm.from(it) }
        }
    }

    override suspend fun find(id: Int): Alarm? = withContext(Dispatchers.IO) {
        dao.find(id)?.let { entity ->
            Alarm.from(entity)
        }
    }

    override suspend fun remove(alarm: Alarm) = withContext(Dispatchers.IO) {
        JetAlarmManager.deleteAlarm(context, alarm)
        dao.delete(
            AlarmEntity(
                id = alarm.id,
                insertedAt = 0L,
                updatedAt = 0L,
            )
        )
    }
}
