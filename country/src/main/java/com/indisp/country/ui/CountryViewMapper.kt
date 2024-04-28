package com.indisp.country.ui

import com.indisp.country.domain.model.Country
import com.indisp.country.domain.model.Filter
import com.indisp.country.ui.model.PopulationFilter
import com.indisp.country.ui.model.PresentableCountry

fun toPresentableCountry(country: Country) =
    PresentableCountry(
        id = country.id,
        name = country.name,
        currency = country.currency,
        capital = country.capital,
        population = country.population,
        flagUrl = country.flagUrl
    )

fun toFilterItem(filter: Filter.Population) = PopulationFilter(
    id = filter.id,
    text = filter.text,
    isSelected = false,
    limit = filter.limit
)