package com.rafih.justfocus.domain.model

import android.app.usage.UsageStats

data class UsageStatsInfo(
    var usageStats: UsageStats,
    var dataChartPercentInfo: DataChartPercentInfo
)
