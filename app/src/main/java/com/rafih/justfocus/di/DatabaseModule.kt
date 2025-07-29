package com.rafih.justfocus.di

import android.content.Context
import androidx.room.Room
import com.rafih.justfocus.data.local.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_db"
        ).build()
    }

    @Provides
    fun provideBlockedAppDao(db: AppDatabase) = db.blockedAppDao()

    @Provides
    fun provideBlockedShortDao(db: AppDatabase) = db.blockedShortDao()

    @Provides
    fun provideFocusHistoryDao(db: AppDatabase) = db.focusHistoryDao()

    @Provides
    fun provideAppTimerDao(db: AppDatabase) = db.appTimerDao()
}