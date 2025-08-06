package com.rafih.justfocus.di

import com.rafih.justfocus.data.repository.AppUsageLimitRepositoryImpl
import com.rafih.justfocus.data.repository.BlockedAppRepositoryImpl
import com.rafih.justfocus.data.repository.BlockedShortRepositoryImpl
import com.rafih.justfocus.data.repository.DataStoreRepositoryImpl
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//this used for accessibility service
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ServiceEntryPoint {
    fun provideBlockedAppRepository(): BlockedAppRepositoryImpl
    fun provideBlockedShortRepository(): BlockedShortRepositoryImpl
    fun provideDataStoreRepository(): DataStoreRepositoryImpl
    fun provideAppTimerRepository(): AppUsageLimitRepositoryImpl
}