package com.rafih.justfocus.domain.model

sealed class UiEvent {
    data class ShowToast(val message: String): UiEvent()
}