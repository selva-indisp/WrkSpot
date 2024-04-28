package com.indisp.country.ui.model

import com.indisp.designsystem.components.textfield.FilterItem

data class PopulationFilter(
    override val id: Int,
    override val text: String,
    override val isSelected: Boolean,
    val limit: Long
) : FilterItem(id, text, isSelected)