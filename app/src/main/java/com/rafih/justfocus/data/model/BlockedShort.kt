package com.rafih.justfocus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "blocked_short")
data class BlockedShort(
    @PrimaryKey
    val packageName: String
)
