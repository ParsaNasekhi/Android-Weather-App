package com.parsanasekhi.androidweatherapp.repository.bookmarked_city

import com.parsanasekhi.androidweatherapp.data.City
import com.parsanasekhi.androidweatherapp.db.local.bookmarked_city.BookmarkedCityEntity
import kotlinx.coroutines.flow.Flow

interface BookmarkedCityRepository {

    suspend fun insertCity(
        city: City
    )

    suspend fun deleteCity(
        city: City
    )

    suspend fun gelAllCities(): Flow<List<City>>

    suspend fun checkCityExist(id: Int): Flow<Boolean>

}