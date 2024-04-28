package com.indisp.country.domain.model

sealed interface Filter {
    data class Search(val query: String): Filter
    data class Population(val id: Int, val limit: Long, val text: String): Filter
}