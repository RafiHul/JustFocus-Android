package com.rafih.justfocus.domain.model

import android.app.usage.UsageStats

data class UsageStatsInfo(
    var appUsageEvent: AppUsageEvent,
    var dataChartPercentInfo: DataChartPercentInfo
)
