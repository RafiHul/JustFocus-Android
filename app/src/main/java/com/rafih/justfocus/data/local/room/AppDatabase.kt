package com.rafih.justfocus.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rafih.justfocus.data.local.room.dao.BlockedAppDao
import com.rafih.justfocus.data.local.room.dao.FocusHistoryDao
import com.rafih.justfocus.data.model.BlockedApp
import com.rafih.justfocus.data.model.FocusHistory

@Database(entities = [BlockedApp::class, FocusHistory::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun blockedAppDao(): BlockedAppDao
    abstract fun focusHistoryDao(): FocusHistoryDao
}