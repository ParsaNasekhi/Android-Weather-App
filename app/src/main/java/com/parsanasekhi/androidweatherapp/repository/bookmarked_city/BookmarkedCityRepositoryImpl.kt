package com.parsanasekhi.androidweatherapp.repository.bookmarked_city

import com.parsanasekhi.androidweatherapp.data.City
import com.parsanasekhi.androidweatherapp.db.local.bookmarked_city.BookmarkedCityDao
import com.parsanasekhi.androidweatherapp.db.local.bookmarked_city.BookmarkedCityEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookmarkedCityRepositoryImpl @Inject constructor(private val bookmarkedCityDao: BookmarkedCityDao) :
    BookmarkedCityRepository {
    override suspend fun insertCity(city: City) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            bookmarkedCityDao.insertCity(
                BookmarkedCityEntity(
                    cityName = city.name,
                    id = city.id
                )
            )
        }
    }

    override suspend fun deleteCity(city: City) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            bookmarkedCityDao.deleteCity(
                BookmarkedCityEntity(
                    city.name,
                    city.id
                )
            )
        }
    }

    override suspend fun gelAllCities(): Flow<List<City>> = flow {
        emit(bookmarkedCityDao.getAllCities())
    }.map { result ->
        val cities = mutableListOf<City>()
        result.forEach {
            cities.add(
                City(
                    name = it.cityName,
                    id = it.id
                )
            )
        }
        cities.toList()
    }.flowOn(Dispatchers.IO)

    override suspend fun checkCityExist(id: Int): Flow<Boolean> = flow {
        emit(
            bookmarkedCityDao.checkCityExist(id) > 0
        )
    }.flowOn(Dispatchers.IO)
}