package com.indisp.designsystem.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import com.indisp.designsystem.R
import com.indisp.designsystem.components.button.DsButton
import com.indisp.designsystem.components.button.DsButtonType
import com.indisp.designsystem.components.image.ScIcon
import com.indisp.designsystem.components.text.ImageConfig
import com.indisp.designsystem.resource.Size
import kotlinx.collections.immutable.PersistentList

@Composable
fun DsSearchField(
    value: String,
    filters: PersistentList<FilterItem>,
    onBackPressed: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFilterClick: (FilterItem) -> Unit = {},
    hint: String = "Search",
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var filterVisibility by remember { mutableStateOf(true) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Size.small)
    ) {
        DsTextField(
            modifier = modifier.onFocusChanged { },
            value = value,
            onValueChange = onValueChange,
            inputType = DsTextInputType.Search,
            hint = hint,
            focusRequester = focusRequester,
            prefix = {
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        onBackPressed()
                    }
                ) {
                    ScIcon(
                        config = ImageConfig.Paint(painterResource(id = R.drawable.ic_search)),
                        tintColor = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = Size.xSmall)
                    )
                }
            },
            suffix = {
                IconButton(
                    onClick = {
                        filterVisibility = !filterVisibility
                    }
                ) {
                    val tintColor = if (filterVisibility)
                        MaterialTheme.colorScheme.onBackground
                    else
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)

                    ScIcon(
                        config = ImageConfig.Paint(painterResource(id = R.drawable.ic_filter)),
                        tintColor = tintColor,
                        modifier = Modifier
                    )
                }
            },
            onDone = { focusManager.clearFocus() }
        )

        Filter(
            visibility = filterVisibility,
            filters = filters,
            onClick = onFilterClick
        )
    }
}

@Composable
private fun Filter(
    visibility: Boolean,
    filters: PersistentList<FilterItem>,
    onClick: (FilterItem) -> Unit
) {
    if (visibility.not() || filters.isEmpty())
        return

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            count = filters.size
        ) {
            val filter = filters[it]
            DsButton(
                type = DsButtonType.Chip(
                    text = filter.text,
                    isSelected = filter.isSelected
                ),
                onClick = { onClick(filter) }
            )
            Spacer(modifier = Modifier.width(Size.medium))
        }
    }
}

open class FilterItem(open val id: Int, open val text: String, open val isSelected: Boolean)