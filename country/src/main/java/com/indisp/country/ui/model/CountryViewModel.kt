package com.indisp.country.ui.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indisp.core.Result
import com.indisp.country.domain.model.Country
import com.indisp.country.domain.model.Filter
import com.indisp.country.domain.repository.CountryRepository
import com.indisp.country.domain.usecase.ApplyFiltersUseCase
import com.indisp.country.domain.usecase.FetchCountriesListUseCase
import com.indisp.country.domain.usecase.GetFiltersUseCase
import com.indisp.country.domain.usecase.ObserveCountriesListUseCase
import com.indisp.network.NetworkFailure
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@FlowPreview
class CountryViewModel(
    private val observeCountriesListUseCase: ObserveCountriesListUseCase,
    private val fetchCountriesListUseCase: FetchCountriesListUseCase,
    private val applyFiltersUseCase: ApplyFiltersUseCase,
    private val getFiltersUseCase: GetFiltersUseCase,
    private val countryViewMapper: (Country) -> PresentableCountry,
    private val filterMapper: (Filter.Population) -> PopulationFilter?
) : ViewModel() {
    private companion object {
        val INITIAL_STATE = State(
            countriesList = persistentListOf(),
            isLoading = false,
            filters = persistentListOf(),
            searchQuery = ""
        )
    }

    private var observeCountryListJob: Job? = null
    private var applyFilterJob: Job? = null
    private val _searchQueryMutableFlow = MutableStateFlow("")
    private val _searchQueryFlow = _searchQueryMutableFlow.debounce(1000).distinctUntilChanged()
    private val _populationFilterFlow = MutableStateFlow<PopulationFilter?>(null)
    private val _appliedFilterFlow =
        combine(_searchQueryFlow, _populationFilterFlow) { query, populationFilter ->
            buildSet {
                add(Filter.Search(query))
                if (populationFilter != null) {
                    add(
                        Filter.Population(
                            populationFilter.id,
                            populationFilter.limit,
                            populationFilter.text
                        )
                    )
                }
            }
        }
    private val _screenStateFlow = MutableStateFlow(INITIAL_STATE)
    val screenState = _screenStateFlow.asStateFlow()
    private val _sideEffectFlow = MutableStateFlow<SideEffect>(SideEffect.Idle)
    val sideEffectFlow = _sideEffectFlow.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            Event.ScreenCreated -> {
                if(isScreenInitialized().not())
                    loadScreenData()
            }
            is Event.OnFilterSelected -> onFilterSelected(event.filterItem)
            is Event.OnSearchQueryChanged -> {
                _screenStateFlow.update { it.copy(searchQuery = event.searchQuery) }
                _searchQueryMutableFlow.update { event.searchQuery }
            }
            Event.OnErrorShown -> _sideEffectFlow.update { SideEffect.Idle }
        }
    }

    private suspend fun fetchCountriesList(): String {
        return when (val result = fetchCountriesListUseCase()) {
            is Result.Error -> {
                when (result.error) {
                    NetworkFailure.ConnectionTimeOut -> "Connection to server timed out. Please try again."
                    NetworkFailure.NoInternet -> "No active internet found. Please check your internet."
                    is NetworkFailure.UnknownFailure -> "Something went wrong. Please try again later."
                }
            }

            is Result.Success -> ""
        }
    }

    private fun fetchFilters(): PersistentList<PopulationFilter> {
        return getFiltersUseCase().filterIsInstance<Filter.Population>().mapNotNull(filterMapper).toPersistentList()
    }

    private fun loadScreenData() {
        observeCountryListJob?.cancel()
        observeCountryListJob = viewModelScope.launch(Dispatchers.IO) {
            observeCountriesListUseCase().collectLatest { countryList ->
                Log.d("CountryViewModel", "loadScreenData: Loaded ${countryList.size}")
                _screenStateFlow.update {
                    it.copy(countriesList = countryList.map(countryViewMapper).toPersistentList())
                }
            }
        }

        applyFilterJob?.cancel()
        applyFilterJob = viewModelScope.launch(Dispatchers.IO) {
            _appliedFilterFlow.collectLatest {
                applyFiltersUseCase(it)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _screenStateFlow.update { it.copy(isLoading = true) }
            val error = fetchCountriesList()
            if (error.isNotEmpty())
                _sideEffectFlow.update { SideEffect.ShowError(error) }
            val filters = fetchFilters()
            _screenStateFlow.update {
                it.copy(
                    isLoading = false,
                    filters = filters
                )
            }
        }
    }

    private fun onFilterSelected(selectedFilter: PopulationFilter) {
        val updatedFilters = state().filters.map {
            if (it == selectedFilter) it.copy(isSelected = !it.isSelected) else it.copy(isSelected = false)
        }.toPersistentList()
        _screenStateFlow.update { it.copy(filters = updatedFilters) }
        _populationFilterFlow.update { updatedFilters.find { it.isSelected } }
    }

    private fun state() = _screenStateFlow.value

    private fun isScreenInitialized() = state().countriesList.isNotEmpty()
}