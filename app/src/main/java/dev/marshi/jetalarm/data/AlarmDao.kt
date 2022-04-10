package dev.marshi.jetalarm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlarmDao: AlarmRepository {

    @Insert
    override suspend fun insert(entity: AlarmEntity)

    @Insert
    override suspend fun update(entity: AlarmEntity)

    @Query("select * from AlarmEntity")
    override suspend fun list(): List<AlarmEntity>
}
