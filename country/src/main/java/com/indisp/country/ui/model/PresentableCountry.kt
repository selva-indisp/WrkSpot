package com.indisp.country.ui.model

data class PresentableCountry(
    val id: Int,
    val name: String,
    val currency: String,
    val capital: String,
    val population: Long,
    val flagUrl: String
)