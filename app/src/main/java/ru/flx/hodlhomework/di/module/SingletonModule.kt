package ru.flx.hodlhomework.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.flx.hodlhomework.repositories.bitcoinj.BitcoinjRepository
import ru.flx.hodlhomework.repositories.notification.NotificationManager
import ru.flx.hodlhomework.repositories.rest_api.ApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiService()
    }

    @Provides
    @Singleton
    fun provideBitcoinjRepository(apiService: ApiService): BitcoinjRepository {
        return BitcoinjRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(): NotificationManager {
        return NotificationManager()
    }
}