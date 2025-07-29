package com.rafih.justfocus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_history")
data class FocusHistory(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val timeMillsThisDay: Long,
    val focusTimeMillsStart: Long,
    val focusTimeMillsStop: Long,
    val day: Int,
    val month: Int,
    val year: Int,
    val activity: String
)
