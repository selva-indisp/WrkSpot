package com.indisp.country.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countryList: List<CountryEntity>)
    @Query("SELECT * FROM countries")
    suspend fun getCountryList(): List<CountryEntity>
}