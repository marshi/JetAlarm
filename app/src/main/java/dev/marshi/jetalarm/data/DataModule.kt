package dev.marshi.jetalarm.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsAlarmRepository(impl: AlarmRepositoryImpl): AlarmRepository

    companion object {
        @Provides
        fun providesDatabase(
            @ApplicationContext context: Context
        ): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "jetalarm").build()
        }

        @Provides
        fun providesAlarmDao(
            db: AppDatabase
        ): AlarmDao {
            return db.alarmDao()
        }
    }
}
