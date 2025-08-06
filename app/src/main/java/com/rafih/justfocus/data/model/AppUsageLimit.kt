package com.rafih.justfocus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "app_usage_limit")
data class AppUsageLimit (
    @PrimaryKey
    val packageName: String,
    val limitMillis: Long,
    val usageMillis: Long = 0L
)