package com.indisp.country.domain.repository

import com.indisp.core.Result
import com.indisp.country.domain.model.Country
import com.indisp.country.domain.model.Filter
import com.indisp.network.NetworkFailure
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    suspend fun getCountryList(): List<Country>
    fun updateFilter(filters: Set<Filter>)
    fun observeCountriesList(): Flow<List<Country>>
    suspend fun fetchCountriesList(): Result<Unit, NetworkFailure>
}