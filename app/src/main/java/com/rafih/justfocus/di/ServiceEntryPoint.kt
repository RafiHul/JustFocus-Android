package com.rafih.justfocus.di

import com.rafih.justfocus.data.local.dao.BlockedAppDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//this used for accessibility service
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ServiceEntryPoint {
    fun provideBlockedAppDaoForService(): BlockedAppDao
}