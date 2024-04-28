package com.indisp.country.domain.usecase

import com.indisp.country.domain.model.SearchFilter

class GetFiltersUseCase {
    operator fun invoke(): List<SearchFilter> {
        return buildList {
            add(SearchFilter(1, "< 1 Million", 1000000))
            add(SearchFilter(2, "< 5 Million", 5000000))
            add(SearchFilter(3, "< 10 Million", 10000000))
        }
    }
}