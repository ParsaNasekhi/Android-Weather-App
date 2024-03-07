package com.parsanasekhi.androidweatherapp.db.local.bookmark

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("bookmarked_cities_table")
data class BookmarkedCityEntity(
    val cityName: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int
)