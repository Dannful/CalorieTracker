package me.dannly.core.util

sealed class UiEvent {
    object Success: UiEvent()
    object NavigateUp: UiEvent()
    data class ShowMessage(val uiText: UiText): UiEvent()
}