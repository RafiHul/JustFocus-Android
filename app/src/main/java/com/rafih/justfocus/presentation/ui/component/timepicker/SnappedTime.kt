package com.rafih.justfocus.presentation.ui.component.timepicker

import java.time.LocalTime

sealed class SnappedTime(val snappedLocalTime: LocalTime, val snappedIndex: Int) {
    data class Hour (val localTime: LocalTime, val index: Int): SnappedTime(localTime, index)
    data class Minute (val localTime: LocalTime, val index: Int): SnappedTime(localTime, index)
    data class Second (val localTime: LocalTime, val index: Int): SnappedTime(localTime, index)
}