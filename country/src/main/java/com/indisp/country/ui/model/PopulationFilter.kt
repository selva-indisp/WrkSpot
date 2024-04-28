package com.indisp.country.ui.model

import com.indisp.country.domain.model.SearchFilter
import com.indisp.designsystem.components.textfield.FilterItem

data class PopulationFilter(
    override val id: Int,
    override val text: String,
    override val isSelected: Boolean,
    val filter: SearchFilter
) : FilterItem(id, text, isSelected)