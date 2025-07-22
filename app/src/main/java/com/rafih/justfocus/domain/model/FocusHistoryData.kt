package com.rafih.justfocus.domain.model

import com.rafih.justfocus.data.model.FocusHistory
import java.util.Calendar

data class FocusHistoryData(
    val calendar: Calendar,
    val focusDurationTotalMills: Long,
    val data: List<FocusHistory>
)
