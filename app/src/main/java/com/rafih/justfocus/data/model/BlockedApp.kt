package com.rafih.justfocus.data.model

import androidx.room.Entity

@Entity(tableName = "blocked_app")
data class BlockedApp (
    val packageName: String
)