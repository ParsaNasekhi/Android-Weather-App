package com.parsanasekhi.androidweatherapp.repository.bookmarked_city

import com.parsanasekhi.androidweatherapp.db.local.bookmarked_city.BookmarkedCityEntity
import kotlinx.coroutines.flow.Flow

interface BookmarkedCityRepository {

    suspend fun insertCity(
        bookmarkedCityEntity: BookmarkedCityEntity
    )

    suspend fun deleteCity(
        bookmarkedCityEntity: BookmarkedCityEntity
    )

    suspend fun gelAllCities(): Flow<List<BookmarkedCityEntity>>

}