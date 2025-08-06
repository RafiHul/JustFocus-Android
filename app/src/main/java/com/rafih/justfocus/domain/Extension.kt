package com.rafih.justfocus.domain

import com.rafih.justfocus.domain.model.UiEvent
import com.rafih.justfocus.domain.model.handle.RoomResult
import kotlinx.coroutines.flow.MutableSharedFlow
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

fun Double.convertToHours(): Double {
    val seconds = this/ 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    return hours
}

fun Calendar.changeTimeToMidNight(){
    this.set(Calendar.HOUR_OF_DAY, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)
}

fun Calendar.getYearMonthDay(): Triple<Int, Int, Int> {
    return Triple(this.get(Calendar.YEAR), this.get(Calendar.MONTH) + 1, this.get(Calendar.DAY_OF_MONTH))
}

fun Calendar.getDayName(): String {
    val sdf = SimpleDateFormat("EEEE", Locale.ENGLISH)
    return sdf.format(this.time)
}

fun Calendar.formatDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    return sdf.format(this.time)
}

fun Calendar.setCalendarTime(hour: Int, minute: Int, second: Int){
    this.set(Calendar.HOUR_OF_DAY, hour)
    this.set(Calendar.MINUTE, minute)
    this.set(Calendar.SECOND, second)
    this.set(Calendar.MILLISECOND, 0)
}

fun Long.formatMillsDurationToString(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val remainSeconds = seconds % 60
    val hours = minutes / 60
    val remainMinutes = minutes % 60

    return when {
        hours > 0 -> "${hours}h ${remainMinutes}m"
        minutes > 0 -> "${minutes}m ${remainSeconds}s"
        else -> "${seconds}s"
    }
}

fun String.formatViewIdToPackageName(basePackageName: String): String {
    return "$basePackageName:id/$this"
}

fun LocalTime.timeToMilis(): Long {
    return (this.hour * 3600 + this.minute * 60 + this.second) * 1000L
}

fun Long.millisToLocalTime(): LocalTime {
    val totalSeconds = this/ 1000
    val hours = (totalSeconds / 3600).toInt()
    val minutes = ((totalSeconds % 3600) / 60).toInt()
    val seconds = (totalSeconds % 60).toInt()
    return LocalTime.of(hours, minutes, seconds)
}


suspend fun RoomResult.handleUiEvent(uiEventState: MutableSharedFlow<UiEvent>, callBackSuccess: () -> Unit){
    when(this) {
        is RoomResult.Failed -> uiEventState.emit(UiEvent.ShowToast(this.message))
        is RoomResult.Success<*> -> callBackSuccess()
    }
}
