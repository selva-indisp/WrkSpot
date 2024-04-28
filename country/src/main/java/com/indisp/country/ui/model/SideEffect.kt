package com.indisp.country.ui.model

sealed interface SideEffect {
    object Idle: SideEffect
    data class ShowError(val message: String): SideEffect
}