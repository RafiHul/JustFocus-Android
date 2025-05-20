package com.rafih.justfocus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_app")
data class BlockedApp (
    @PrimaryKey
    val packageName: String
)