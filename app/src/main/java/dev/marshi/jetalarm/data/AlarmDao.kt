package dev.marshi.jetalarm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert
    suspend fun insert(entity: AlarmEntity)

    @Update
    suspend fun update(entity: AlarmEntity)

    @Delete
    suspend fun delete(entity: AlarmEntity)

    @Query("select * from AlarmEntity order by inserted_at desc")
    fun list(): Flow<List<AlarmEntity>>

    @Query("select * from AlarmEntity where id = :id")
    fun find(id: String): AlarmEntity?
}
