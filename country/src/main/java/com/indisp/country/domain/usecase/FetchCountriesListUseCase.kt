package com.indisp.country.domain.usecase

import com.indisp.country.domain.repository.CountryRepository

class FetchCountriesListUseCase(
    private val countryRepository: CountryRepository
) {
    suspend operator fun invoke() = countryRepository.fetchCountriesList()
}