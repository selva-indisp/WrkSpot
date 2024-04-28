package com.indisp.country.domain.model

data class Country(
    val id: Int,
    val name: String,
    val currency: String,
    val capital: String,
    val population: Long,
    val flagUrl: String
)