package com.rafih.justfocus.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rafih.justfocus.data.local.room.dao.BlockedAppDao
import com.rafih.justfocus.data.model.BlockedApp

@Database(entities = [BlockedApp::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun blockedAppDao(): BlockedAppDao
}