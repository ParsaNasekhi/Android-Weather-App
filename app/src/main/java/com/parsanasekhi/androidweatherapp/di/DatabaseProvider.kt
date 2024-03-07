package com.parsanasekhi.androidweatherapp.di

import android.content.Context
import androidx.room.Room
import com.parsanasekhi.androidweatherapp.db.local.WeatherAppDatabase
import com.parsanasekhi.androidweatherapp.db.local.bookmarked_city.BookmarkedCityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseProvider {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeatherAppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WeatherAppDatabase::class.java,
            "WeatherAppDatabase.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBookmarkedCityDao(weatherAppDatabase: WeatherAppDatabase): BookmarkedCityDao {
        return weatherAppDatabase.bookmarkedCityDao()
    }

}