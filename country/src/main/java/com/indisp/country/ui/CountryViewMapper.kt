package com.indisp.country.ui

import com.indisp.country.domain.model.Country
import com.indisp.country.domain.model.SearchFilter
import com.indisp.country.ui.model.PopulationFilter
import com.indisp.country.ui.model.PresentableCountry
import com.indisp.designsystem.components.textfield.FilterItem

fun toPresentableCountry(country: Country) =
    PresentableCountry(
        id = country.id,
        name = country.name,
        currency = country.currency,
        capital = country.capital,
        population = country.population,
        flagUrl = country.flagUrl
    )

fun toFilterItem(filter: SearchFilter) = PopulationFilter(
    id = filter.id,
    text = filter.name,
    isSelected = false,
    filter = filter
)