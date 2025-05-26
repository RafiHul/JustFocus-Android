package com.rafih.justfocus.di

import com.rafih.justfocus.data.repository.BlockedAppRepository
import com.rafih.justfocus.data.repository.DataStoreRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//this used for accessibility service
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ServiceEntryPoint {
    fun provideBlockedAppRepository(): BlockedAppRepository
    fun provideDataStoreRepository(): DataStoreRepository
}