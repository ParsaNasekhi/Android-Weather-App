package com.parsanasekhi.androidweatherapp.di

import com.parsanasekhi.androidweatherapp.db.remote.current_weather.WeatherApiService
import com.parsanasekhi.androidweatherapp.repository.current_weather.CurrentWeatherRepository
import com.parsanasekhi.androidweatherapp.repository.current_weather.CurrentWeatherRepositoryImpl
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
    fun provideCurrentWeatherRepository(weatherApiService: WeatherApiService): CurrentWeatherRepository {
        return CurrentWeatherRepositoryImpl(weatherApiService)
    }

}