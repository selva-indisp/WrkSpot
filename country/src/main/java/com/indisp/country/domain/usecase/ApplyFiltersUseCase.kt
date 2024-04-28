package com.indisp.country.domain.usecase

import com.indisp.country.domain.model.Filter
import com.indisp.country.domain.repository.CountryRepository

class ApplyFiltersUseCase(
    private val countriesRepository: CountryRepository
) {
    operator fun invoke(filters: Set<Filter>) {
        countriesRepository.updateFilter(filters)
    }
}