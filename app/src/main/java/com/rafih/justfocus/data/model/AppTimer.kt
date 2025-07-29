package com.rafih.justfocus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "app_timer")
data class AppTimer (
    @PrimaryKey
    val packageName: String,
    val blockUntilMills: Long
)