package com.indisp.country.ui.model

import kotlinx.collections.immutable.PersistentList

data class State(
    val countriesList: PersistentList<PresentableCountry>,
    val searchQuery: String,
    val filters: PersistentList<PopulationFilter>,
    val isLoading: Boolean
)