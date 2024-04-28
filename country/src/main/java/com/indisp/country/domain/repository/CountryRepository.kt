package com.indisp.country.domain.repository

import com.indisp.country.domain.model.Country

interface CountryRepository {
    suspend fun getCountryList(): List<Country>
}