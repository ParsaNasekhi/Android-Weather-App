package com.parsanasekhi.androidweatherapp.db.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.parsanasekhi.androidweatherapp.db.local.bookmarked_city.BookmarkedCityDao
import com.parsanasekhi.androidweatherapp.db.local.bookmarked_city.BookmarkedCityEntity

@Database(entities = [BookmarkedCityEntity::class], version = 1, exportSchema = false)
abstract class WeatherAppDatabase : RoomDatabase() {

    abstract fun bookmarkedCityDao(): BookmarkedCityDao

}