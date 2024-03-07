package com.parsanasekhi.androidweatherapp.repository.bookmarked_city

import com.parsanasekhi.androidweatherapp.db.local.bookmarked_city.BookmarkedCityDao
import com.parsanasekhi.androidweatherapp.db.local.bookmarked_city.BookmarkedCityEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookmarkedCityRepositoryImpl @Inject constructor(private val bookmarkedCityDao: BookmarkedCityDao) : BookmarkedCityRepository {
    override suspend fun insertCity(bookmarkedCityEntity: BookmarkedCityEntity) {
        bookmarkedCityDao.insertCity(bookmarkedCityEntity)
    }

    override suspend fun deleteCity(bookmarkedCityEntity: BookmarkedCityEntity) {
        bookmarkedCityDao.deleteCity(bookmarkedCityEntity)
    }

    override suspend fun gelAllCities(): Flow<List<BookmarkedCityEntity>> = flow {
        emit(bookmarkedCityDao.getAllCities())
    }
}