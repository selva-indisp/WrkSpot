package com.indisp.country.data

import com.indisp.country.data.local.CountryEntity
import com.indisp.country.data.network.CountryDto
import com.indisp.country.domain.model.Country

fun toCountry(countryEntity: CountryEntity): Country {
    return Country(
        id = countryEntity.id,
        name = countryEntity.name ?: "",
        currency = countryEntity.code ?: "",
        capital = countryEntity.capital ?: "",
        population = countryEntity.population ?: 0L,
        flagUrl = countryEntity.flagUrl ?: ""
    )
}

fun toEntity(countryDto: CountryDto) = CountryEntity(
    id = countryDto.id ?: 0,
    name = countryDto.name,
    code = countryDto.currency,
    capital = countryDto.capital,
    population = countryDto.population,
    flagUrl = countryDto.media?.flagUrl
)