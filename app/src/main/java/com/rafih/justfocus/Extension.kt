package com.rafih.justfocus

import com.rafih.justfocus.domain.model.UiEvent
import com.rafih.justfocus.domain.util.RoomResult
import kotlinx.coroutines.flow.MutableSharedFlow
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


suspend fun RoomResult.handleUiEvent(uiEventState: MutableSharedFlow<UiEvent>, callBackSuccess: () -> Unit){
    when(this) {
        is RoomResult.Failed -> uiEventState.emit(UiEvent.ShowToast(this.message))
        is RoomResult.Success<*> -> callBackSuccess()
    }
}