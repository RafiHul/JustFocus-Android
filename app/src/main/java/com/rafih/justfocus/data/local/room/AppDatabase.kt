package com.rafih.justfocus.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rafih.justfocus.data.local.room.dao.AppUsageLimitDao
import com.rafih.justfocus.data.local.room.dao.BlockedAppDao
import com.rafih.justfocus.data.local.room.dao.BlockedShortDao
import com.rafih.justfocus.data.local.room.dao.FocusHistoryDao
import com.rafih.justfocus.data.model.AppUsageLimit
import com.rafih.justfocus.data.model.BlockedApp
import com.rafih.justfocus.data.model.BlockedShort
import com.rafih.justfocus.data.model.FocusHistory

@Database(entities = [BlockedApp::class, FocusHistory::class, BlockedShort::class, AppUsageLimit::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun blockedAppDao(): BlockedAppDao
    abstract fun blockedShortDao(): BlockedShortDao
    abstract fun focusHistoryDao(): FocusHistoryDao
    abstract fun appTimerDao(): AppUsageLimitDao
}