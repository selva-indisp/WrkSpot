package com.indisp.country.domain.usecase

import com.indisp.country.domain.repository.CountryRepository

class ObserveCountriesListUseCase(
    private val countryRepository: CountryRepository
) {
    operator fun invoke() = countryRepository.observeCountriesList()
}