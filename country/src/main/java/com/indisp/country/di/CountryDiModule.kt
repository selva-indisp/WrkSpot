package com.indisp.country.di

import com.indisp.country.data.CountryRepositoryImpl
import com.indisp.country.data.network.CountryApi
import com.indisp.country.data.toCountry
import com.indisp.country.data.toEntity
import com.indisp.country.domain.repository.CountryRepository
import com.indisp.country.domain.usecase.GetCountriesListUseCase
import com.indisp.country.domain.usecase.GetFiltersUseCase
import com.indisp.country.ui.model.CountryViewModel
import com.indisp.country.ui.toFilterItem
import com.indisp.country.ui.toPresentableCountry
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryDiModule = module {
    viewModel<CountryViewModel> {
        CountryViewModel(
            getCountriesListUseCase = get(),
            getFiltersUseCase = get(),
            countryViewMapper = ::toPresentableCountry,
            filterMapper = ::toFilterItem
        )
    }

    factory { GetCountriesListUseCase(get()) }
    factory { GetFiltersUseCase() }
    factory<CountryRepository> { CountryRepositoryImpl(get(), get(), ::toCountry, ::toEntity) }
    factory { CountryApi(get()) }
}
