package com.rafih.justfocus

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Double.convertToHours(): Double {
    val seconds = this/ 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    return hours
}

fun Calendar.getDayName(): String {
    val sdf = SimpleDateFormat("EEEE", Locale.ENGLISH)
    return sdf.format(this.time)
}

fun Calendar.setCalendarTime(hour: Int, minute: Int, second: Int){
    this.set(Calendar.HOUR_OF_DAY, hour)
    this.set(Calendar.MINUTE, minute)
    this.set(Calendar.SECOND, second)
    this.set(Calendar.MILLISECOND, 0)
}