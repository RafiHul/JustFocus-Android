package com.rafih.justfocus.domain.model.handle

sealed class RoomResult {
    data class Success<T>(val data: T?): RoomResult()
    data class Failed(val message: String): RoomResult()
}