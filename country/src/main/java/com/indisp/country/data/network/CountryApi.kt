package com.indisp.country.data.network

import com.indisp.core.Result
import com.indisp.network.NetworkApiService
import com.indisp.network.NetworkFailure

class CountryApi(
    private val networkApiService: NetworkApiService
) {
    suspend fun getAllCountriesList(): Result<List<CountryDto>, NetworkFailure> {
        val url = "https://api.sampleapis.com/countries/countries"
        return networkApiService.get<List<CountryDto>>(url)
    }
}