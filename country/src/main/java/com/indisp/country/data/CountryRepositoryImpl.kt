package com.indisp.country.data

import android.util.Log
import com.indisp.core.Result
import com.indisp.country.data.local.CountryDao
import com.indisp.country.data.local.CountryEntity
import com.indisp.country.data.network.CountryApi
import com.indisp.country.data.network.CountryDto
import com.indisp.country.domain.model.Country
import com.indisp.country.domain.repository.CountryRepository

class CountryRepositoryImpl(
    private val countryNetworkApi: CountryApi,
    private val countryDao: CountryDao,
    private val countryDomainMapper: (CountryEntity) -> Country,
    private val countryEntityMapper: (CountryDto) -> CountryEntity
) : CountryRepository {
    override suspend fun getCountryList(): List<Country> {
        val cachedCountryList = countryDao.getCountryList()
        if (cachedCountryList.isNotEmpty())
            return cachedCountryList.map(countryDomainMapper)

        return when (val result = countryNetworkApi.getAllCountriesList()) {
            is Result.Error -> {
                Log.d("CountryRepositoryImpl", "getCountryList: Error in fetching list - ${result.error}")
                emptyList()
            }
            is Result.Success -> {
                val entityList = result.data.map(countryEntityMapper)
                countryDao.insertAll(entityList)
                countryDao.getCountryList().map(countryDomainMapper)
            }
        }
    }
}