package com.indisp.country.domain.usecase

import com.indisp.country.domain.model.Country
import com.indisp.country.domain.repository.CountryRepository

class GetCountriesListUseCase(
    private val countryRepository: CountryRepository
) {
    suspend operator fun invoke(): List<Country> {
        return countryRepository.getCountryList()
    }
}