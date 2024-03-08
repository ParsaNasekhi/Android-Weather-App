package com.parsanasekhi.androidweatherapp.db.local.bookmarked_city

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookmarkedCityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(bookmarkedCandidate: BookmarkedCityEntity)

    @Delete
    suspend fun deleteCity(bookmarkedCandidate: BookmarkedCityEntity)

    @Query("SELECT * FROM bookmarked_cities_table ORDER BY cityName")
    suspend fun getAllCities(): List<BookmarkedCityEntity>

    @Query("SELECT COUNT(*) FROM bookmarked_cities_table WHERE :id = id")
    suspend fun checkCityExist(id: Int): Int

}