package com.rafih.justfocus.domain

import java.util.Calendar

fun Calendar.changeTimeToMidNight(){
    this.set(Calendar.HOUR_OF_DAY, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)
}

fun Calendar.getYearMonthDay(): Triple<Int, Int, Int> {
    return Triple(this.get(Calendar.YEAR), this.get(Calendar.MONTH) + 1, this.get(Calendar.DAY_OF_MONTH))
}
