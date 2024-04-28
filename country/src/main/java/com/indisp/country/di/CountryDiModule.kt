package com.indisp.country.di

import com.indisp.country.data.CountryRepositoryImpl
import com.indisp.country.data.network.CountryApi
import com.indisp.country.data.toCountry
import com.indisp.country.data.toEntity
import com.indisp.country.domain.repository.CountryRepository
import com.indisp.country.domain.usecase.ApplyFiltersUseCase
import com.indisp.country.domain.usecase.FetchCountriesListUseCase
import com.indisp.country.domain.usecase.GetFiltersUseCase
import com.indisp.country.domain.usecase.ObserveCountriesListUseCase
import com.indisp.country.ui.model.CountryViewModel
import com.indisp.country.ui.toFilterItem
import com.indisp.country.ui.toPresentableCountry
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@OptIn(FlowPreview::class)
val countryDiModule = module {
    viewModel<CountryViewModel> {
        CountryViewModel(
            get(), get(), get(), get(),
            countryViewMapper = ::toPresentableCountry,
            filterMapper = ::toFilterItem
        )
    }

    factory { GetFiltersUseCase() }
    factory { ApplyFiltersUseCase(get()) }
    factory { FetchCountriesListUseCase(get()) }
    factory { ObserveCountriesListUseCase(get()) }
    single<CountryRepository> { CountryRepositoryImpl(get(), get(), ::toCountry, ::toEntity) }
    factory { CountryApi(get()) }
}
