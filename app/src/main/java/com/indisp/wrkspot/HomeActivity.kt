package com.indisp.wrkspot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.indisp.country.ui.model.CountryViewModel
import com.indisp.country.ui.view.CountryListScreen
import com.indisp.designsystem.theme.DsTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DsTheme {
                val countryViewModel: CountryViewModel = koinViewModel()
                CountryListScreen(
                    stateFlow = countryViewModel.screenState,
                    onEvent = countryViewModel::onEvent
                )
            }
        }
    }
}