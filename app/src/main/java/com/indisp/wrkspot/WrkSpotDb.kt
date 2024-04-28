package com.indisp.wrkspot

import androidx.room.Database
import androidx.room.RoomDatabase
import com.indisp.country.data.local.CountryDao
import com.indisp.country.data.local.CountryEntity

@Database(entities = [CountryEntity::class], version = 1)
abstract class WrkSpotDb : RoomDatabase() {
    abstract fun getCountryDao(): CountryDao
}