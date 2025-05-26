package com.rafih.justfocus.di

import com.rafih.justfocus.data.repository.UsageStatsRepository
import com.rafih.justfocus.data.repository.UsageStatsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//this just for test
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUsageStatsRepository(): UsageStatsRepository = UsageStatsRepositoryImpl()
}