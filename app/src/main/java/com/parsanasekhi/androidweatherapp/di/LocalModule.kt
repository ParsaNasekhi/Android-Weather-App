package com.parsanasekhi.androidweatherapp.di

import com.parsanasekhi.androidweatherapp.db.local.bookmarked_city.BookmarkedCityDao
import com.parsanasekhi.androidweatherapp.db.remote.weather.WeatherApiService
import com.parsanasekhi.androidweatherapp.repository.bookmarked_city.BookmarkedCityRepository
import com.parsanasekhi.androidweatherapp.repository.bookmarked_city.BookmarkedCityRepositoryImpl
import com.parsanasekhi.androidweatherapp.repository.weather.WeatherRepository
import com.parsanasekhi.androidweatherapp.repository.weather.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideCurrentWeatherRepository(weatherApiService: WeatherApiService): WeatherRepository {
        return WeatherRepositoryImpl(weatherApiService)
    }

    @Provides
    @Singleton
    fun provideBookmarkedCityRepository(bookmarkedCityDao: BookmarkedCityDao): BookmarkedCityRepository {
        return BookmarkedCityRepositoryImpl(bookmarkedCityDao)
    }

}