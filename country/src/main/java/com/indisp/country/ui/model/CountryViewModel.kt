package com.indisp.country.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indisp.country.domain.model.Country
import com.indisp.country.domain.model.SearchFilter
import com.indisp.country.domain.usecase.GetCountriesListUseCase
import com.indisp.country.domain.usecase.GetFiltersUseCase
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountryViewModel(
    private val getCountriesListUseCase: GetCountriesListUseCase,
    private val getFiltersUseCase: GetFiltersUseCase,
    private val countryViewMapper: (Country) -> PresentableCountry,
    private val filterMapper: (SearchFilter) -> PopulationFilter
) : ViewModel() {
    private companion object {
        val INITIAL_STATE = State(
            countriesList = persistentListOf(),
            isLoading = false,
            error = "",
            filters = persistentListOf(),
            searchQuery = ""
        )
    }

    private var _countriesList = persistentListOf<PresentableCountry>()
    private val _screenStateFlow = MutableStateFlow(INITIAL_STATE)
    val screenState = _screenStateFlow.asStateFlow()
    fun onEvent(event: Event) {
        when (event) {
            Event.ScreenCreated -> loadScreenData()
            is Event.OnFilterSelected -> onFilterSelected(event.filterItem)
            is Event.OnSearchQueryChanged -> {}
        }
    }

    private suspend fun fetchCountriesList(): PersistentList<PresentableCountry> {
        return getCountriesListUseCase().map { countryViewMapper(it) }.toPersistentList()
    }

    private fun fetchFilters(): PersistentList<PopulationFilter> {
        return getFiltersUseCase().map(filterMapper).toPersistentList()
    }

    private fun loadScreenData() {
        viewModelScope.launch(Dispatchers.IO) {
            _screenStateFlow.update { it.copy(isLoading = true) }
            _countriesList = fetchCountriesList()
            val filters = fetchFilters()
            _screenStateFlow.update {
                it.copy(
                    isLoading = false,
                    countriesList = _countriesList,
                    filters = filters
                )
            }
        }
    }

    private fun onFilterSelected(selectedFilter: PopulationFilter) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedFilters = _screenStateFlow.value.filters.map {
                if (it.id == selectedFilter.id) it.copy(isSelected = !selectedFilter.isSelected) else it.copy(
                    isSelected = false
                )
            }
            _screenStateFlow.update { it.copy(filters = updatedFilters.toPersistentList()) }
            val filteredCountriesList =
                _countriesList.filter { it.population in 1L..selectedFilter.filter.limit }
            _screenStateFlow.update { it.copy(countriesList = filteredCountriesList.toPersistentList()) }
        }
    }
}