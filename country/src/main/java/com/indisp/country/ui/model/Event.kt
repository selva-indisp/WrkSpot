package com.indisp.country.ui.model

sealed interface Event {
    object ScreenCreated: Event
    data class OnFilterSelected(val filterItem: PopulationFilter): Event
    data class OnSearchQueryChanged(val searchQuery: String): Event
}