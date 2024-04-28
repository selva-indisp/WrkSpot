package com.indisp.country.ui.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.indisp.country.R
import com.indisp.country.ui.model.Event
import com.indisp.country.ui.model.PopulationFilter
import com.indisp.country.ui.model.PresentableCountry
import com.indisp.country.ui.model.SideEffect
import com.indisp.country.ui.model.State
import com.indisp.designsystem.components.composable.LifecycleObserver
import com.indisp.designsystem.components.loader.DsLoader
import com.indisp.designsystem.components.text.DsText
import com.indisp.designsystem.components.text.DsTextType
import com.indisp.designsystem.components.text.ImageConfig
import com.indisp.designsystem.components.textfield.DsSearchField
import com.indisp.designsystem.components.textfield.FilterItem
import com.indisp.designsystem.resource.Size
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CountryListScreen(
    stateFlow: StateFlow<State>,
    sideEffectFlow: StateFlow<SideEffect>,
    onEvent: (Event) -> Unit,
) {
    val state by stateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        sideEffectFlow.collectLatest { sideEffect ->
            when (sideEffect) {
                is SideEffect.ShowError -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_LONG).show()
                    onEvent(Event.OnErrorShown)
                }
                else -> {}
            }
        }
    }

    LifecycleObserver(onCreate = { onEvent(Event.ScreenCreated) })

    Scaffold { padding ->
        if (state.isLoading) DsLoader()
        Column {
            Spacer(modifier = Modifier.height(Size.large))
            SearchField(
                searchQuery = state.searchQuery, filters = state.filters, onEvent = onEvent
            )
            CountryList(
                state.countriesList, Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun SearchField(
    searchQuery: String, filters: PersistentList<FilterItem>, onEvent: (Event) -> Unit
) {
    DsSearchField(value = searchQuery,
        filters = filters,
        onBackPressed = { },
        onValueChange = { onEvent(Event.OnSearchQueryChanged(it)) },
        hint = "Search Country",
        onFilterClick = { onEvent(Event.OnFilterSelected(it as PopulationFilter)) })
}

@Composable
private fun CountryList(countries: PersistentList<PresentableCountry>, modifier: Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(Size.small)
    ) {
        items(count = countries.size, key = { countries[it].id }) {
            val country = countries[it]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Size.small, vertical = Size.xSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = country.flagUrl,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                    error = painterResource(id = R.drawable.ic_launcher_foreground),
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(Size.large))
                Column(
                    modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
                ) {
                    DsText(
                        text = country.name, type = DsTextType.Primary()
                    )
                    if (country.capital.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(Size.xSmall))
                        DsText(
                            text = country.capital, type = DsTextType.Secondary()
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Center
                ) {
                    DsText(
                        text = country.currency,
                        type = DsTextType.Ternary(suffixIcon = ImageConfig.Paint(painterResource(id = R.drawable.ic_currency)))
                    )
                    if (country.population != 0L) {
                        Spacer(modifier = Modifier.height(Size.xSmall))
                        DsText(
                            text = country.population.toString(),
                            type = DsTextType.Ternary(suffixIcon = ImageConfig.Vector(Icons.Default.Person))
                        )
                    }
                }
            }
        }
    }
}