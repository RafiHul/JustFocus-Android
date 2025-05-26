package com.rafih.justfocus.domain.model

import java.util.Calendar

data class AppUsageGroup(
    val date: Calendar,
    val events: List<AppUsageEvent>,
    val listIndex: Int
)
