package com.parsanasekhi.androidweatherapp.db.local.bookmark

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

    @Query("SELECT * FROM bookmarked_cities_table")
    suspend fun getAllCities(): List<BookmarkedCityEntity>

}