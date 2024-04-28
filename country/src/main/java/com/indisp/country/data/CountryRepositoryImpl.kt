package com.indisp.country.data

import android.util.Log
import com.indisp.core.Result
import com.indisp.country.data.local.CountryDao
import com.indisp.country.data.local.CountryEntity
import com.indisp.country.data.network.CountryApi
import com.indisp.country.data.network.CountryDto
import com.indisp.country.domain.model.Country
import com.indisp.country.domain.model.Filter
import com.indisp.country.domain.repository.CountryRepository
import com.indisp.network.NetworkFailure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class CountryRepositoryImpl(
    private val countryNetworkApi: CountryApi,
    private val countryDao: CountryDao,
    private val countryDomainMapper: (CountryEntity) -> Country,
    private val countryEntityMapper: (CountryDto) -> CountryEntity
) : CountryRepository {

    private val appliedFiltersFlow = MutableStateFlow<Set<Filter>>(emptySet())
    private val countryListFlow = appliedFiltersFlow.flatMapLatest { filters ->
        Log.d("CountryRepository", "Filters: $filters")
        if (filters.isEmpty())
            countryDao.getCountryList().map { it.map(countryDomainMapper) }
        else {
            val searchFilter = filters.filterIsInstance<Filter.Search>().firstOrNull()?.query
            val populationLimit =
                filters.filterIsInstance<Filter.Population>().maxOfOrNull { it.limit }
            countryDao.filterCountryList(name = searchFilter, populationLimit = populationLimit).map { it.map(countryDomainMapper) }
        }
    }

    override suspend fun getCountryList(): List<Country> {
        return emptyList()
    }

    override fun updateFilter(filters: Set<Filter>) {
        Log.d("CountryRepository", "updateFilter: Filters $filters")
        appliedFiltersFlow.value = filters
    }

    override fun observeCountriesList(): Flow<List<Country>> = countryListFlow

    override suspend fun fetchCountriesList(): Result<Unit, NetworkFailure> {
        return when (val result = countryNetworkApi.getAllCountriesList()) {
            is Result.Error -> result
            is Result.Success -> {
                val entityList = result.data.map(countryEntityMapper)
                countryDao.insertAll(entityList)
                Result.Success(Unit)
            }
        }
    }
}