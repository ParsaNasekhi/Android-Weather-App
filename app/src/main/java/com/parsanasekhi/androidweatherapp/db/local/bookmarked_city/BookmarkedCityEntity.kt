package com.parsanasekhi.androidweatherapp.db.local.bookmarked_city

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("bookmarked_cities_table")
data class BookmarkedCityEntity(
    val cityName: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int
)