package com.indisp.country.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countryList: List<CountryEntity>)
    @Query("SELECT * FROM countries")
    fun getCountryList(): Flow<List<CountryEntity>>

    @Query("SELECT * FROM countries WHERE (:name IS NULL OR LOWER(NAME) LIKE '%' || LOWER(:name) || '%') AND (:populationLimit IS NULL OR population < :populationLimit)")
    fun filterCountryList(name: String?, populationLimit: Long?): Flow<List<CountryEntity>>
}