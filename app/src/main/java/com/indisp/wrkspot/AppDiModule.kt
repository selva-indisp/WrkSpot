package com.indisp.wrkspot

import androidx.room.Room
import com.indisp.country.data.local.CountryDao
import org.koin.dsl.module

val appDiModule = module {
    single<WrkSpotDb> {
        Room.databaseBuilder(
            context = get(),
            WrkSpotDb::class.java,
            "WrkSpotDb"
        ).build()
    }

    factory<CountryDao> {
        get<WrkSpotDb>().getCountryDao()
    }
}