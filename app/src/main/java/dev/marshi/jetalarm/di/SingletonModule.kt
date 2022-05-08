package dev.marshi.jetalarm.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Provides
    fun providesSingletonScope(): SingletonScope {
        return object : SingletonScope {
            override val coroutineContext: CoroutineContext
                get() = SupervisorJob()
        }
    }
}

interface SingletonScope : CoroutineScope
