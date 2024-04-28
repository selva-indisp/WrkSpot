package com.indisp.country.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    val id: Int? = null,
    val name: String? = null,
    val currency: String? = null,
    val capital: String? = null,
    @SerialName("population")
    val population: Long? = null,
    @SerialName("media")
    val media: Media? = null
) {
    @Serializable
    class Media(
        @SerialName("flag")
        val flagUrl: String?
    )
}