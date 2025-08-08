package com.rafih.justfocus.domain.model

data class FocusModeSessionInfo(
    var startMills: Long = 0L,
    var stopMills: Long = 0L,
    var activity: String
)