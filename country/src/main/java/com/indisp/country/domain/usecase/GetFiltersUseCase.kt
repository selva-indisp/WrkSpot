package com.indisp.country.domain.usecase

import com.indisp.country.domain.model.Filter

class GetFiltersUseCase {
    operator fun invoke(): Set<Filter> {
        return buildSet {
            add(Filter.Population(1, 1000000, "< 1 Million"))
            add(Filter.Population(2, 5000000, "< 5 Million"))
            add(Filter.Population(3, 10000000, "< 10 Million"))
        }
    }
}