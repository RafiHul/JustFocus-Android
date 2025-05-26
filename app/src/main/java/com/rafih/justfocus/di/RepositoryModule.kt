package com.rafih.justfocus.di

import com.rafih.justfocus.data.local.dao.BlockedAppDao
import com.rafih.justfocus.data.local.repository.BlockedAppRepository
import com.rafih.justfocus.data.repository.UsageStatsRepository
import com.rafih.justfocus.data.repository.UsageStatsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideBlockedAppRepository(dao: BlockedAppDao) = BlockedAppRepository(dao)

    @Provides
    @Singleton
    fun provideUsageStatsRepository(): UsageStatsRepository = UsageStatsRepositoryImpl()
}