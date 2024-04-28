package com.indisp.country.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey
    val id: Int,
    val name: String? = null,
    val code: String? = null,
    val capital: String? = null,
    val population: Long? = null,
    val flagUrl: String? = null
)