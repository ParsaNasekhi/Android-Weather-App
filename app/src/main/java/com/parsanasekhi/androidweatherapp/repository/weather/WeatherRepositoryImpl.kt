package com.parsanasekhi.androidweatherapp.repository.weather

import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.data.ForecastWeather
import com.parsanasekhi.androidweatherapp.data.Location
import com.parsanasekhi.androidweatherapp.db.remote.ApiUrl
import com.parsanasekhi.androidweatherapp.db.remote.weather.WeatherApiService
import com.parsanasekhi.androidweatherapp.utills.formatUnixTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherRepositoryImpl @Inject constructor(private val weatherApiService: WeatherApiService) :
    WeatherRepository {

    override suspend fun getCurrentWeather(
        cityName: String
    ): Flow<CurrentWeather> = flow {
        emit(weatherApiService.getCurrentWeather(cityName))
    }.filter { response ->
        if (!response.isSuccessful) {
            throw Exception(response.message())
        } else if (response.body() == null) {
            throw Exception("The response body was null!")
        }
        response.isSuccessful && response.body() != null
    }.map { response ->
        CurrentWeather(
            cityName = response.body()!!.name,
            location = Location(
                lat = response.body()!!.coord.lat.toString(),
                lon = response.body()!!.coord.lon.toString()
            ),
            icon = response.body()!!.weather[0].icon,
            description = response.body()!!.weather[0].description,
            temp = "${(response.body()!!.main.temp - 273.15).roundToInt()}°C",
            minTemp = "${(response.body()!!.main.tempMin - 273.15).roundToInt()}°C",
            maxTemp = "${(response.body()!!.main.tempMax - 273.15).roundToInt()}°C",
            humidity = "${response.body()!!.main.humidity}%",
            sunset = formatUnixTime(response.body()!!.sys.sunset.toString(), "hh:mm a"),
            sunrise = formatUnixTime(response.body()!!.sys.sunrise.toString(), "hh:mm a"),
            windSpeed = "${response.body()!!.wind.speed} m/s",
            country = response.body()!!.sys.country,
            date = formatUnixTime(response.body()!!.dt.toString(), "MMM/dd/YYYY"),
            time = formatUnixTime(response.body()!!.dt.toString(), "hh:mm a"),
            id = response.body()!!.id
        )
    }.flowOn(Dispatchers.IO)

    override suspend fun getForecastWeather(
        cityName: String,
        daysCount: String
    ): Flow<ForecastWeather> = flow {
        emit(weatherApiService.getForecastWeather(cityName))
    }.filter { response ->
        if (!response.isSuccessful) {
            throw Exception(response.message())
        } else if (response.body() == null) {
            throw Exception("The response body was null!")
        }
        response.isSuccessful && response.body() != null
    }.map { response ->
        val forecastWeather = ForecastWeather()
        response.body()!!.list.forEach {
            forecastWeather.add(
                ForecastWeather.Detail(
                    temp = "${(it.main.temp - 273.15).roundToInt()}°C",
                    date = formatUnixTime(it.dt.toString(), "MMM dd"),
                    time = formatUnixTime(it.dt.toString(), "hh:mm a"),
                    icon = "${ApiUrl.LoadImageUrl}${it.weather[0].icon}.png",
                    minTemp = "${(it.main.tempMin - 273.15).roundToInt()}°C",
                    maxTemp = "${(it.main.tempMax - 273.15).roundToInt()}°C",
                    humidity = "${it.main.humidity}%",
                    windSpeed = "${it.wind.speed} m/s",
                    sunset = formatUnixTime(
                        response.body()!!.city.sunset.toString(),
                        "hh:mm a"
                    ),
                    sunrise = formatUnixTime(
                        response.body()!!.city.sunrise.toString(),
                        "hh:mm a"
                    ),
                    description = it.weather[0].description
                )
            )
        }
        forecastWeather
    }.flowOn(Dispatchers.IO)

    override suspend fun getCityWeatherById(id: Int) = flow {
        emit(weatherApiService.getCityWeatherById(id))
    }.filter { response ->
        if (!response.isSuccessful) {
            throw Exception(response.message())
        } else if (response.body() == null) {
            throw Exception("The response body was null!")
        }
        response.isSuccessful && response.body() != null
    }.map { response ->
        CurrentWeather(
            cityName = response.body()!!.name,
            location = Location(
                lat = response.body()!!.coord.lat.toString(),
                lon = response.body()!!.coord.lon.toString()
            ),
            icon = "${ApiUrl.LoadImageUrl}${response.body()!!.weather[0].icon}.png",
            description = response.body()!!.weather[0].description,
            temp = "${(response.body()!!.main.temp - 273.15).roundToInt()}°C",
            minTemp = "${(response.body()!!.main.tempMin - 273.15).roundToInt()}°C",
            maxTemp = "${(response.body()!!.main.tempMax - 273.15).roundToInt()}°C",
            humidity = "${response.body()!!.main.humidity}%",
            sunset = formatUnixTime(response.body()!!.sys.sunset.toString(), "hh:mm a"),
            sunrise = formatUnixTime(response.body()!!.sys.sunrise.toString(), "hh:mm a"),
            windSpeed = "${response.body()!!.wind.speed} m/s",
            country = response.body()!!.sys.country,
            date = formatUnixTime(response.body()!!.dt.toString(), "MMM/dd/YYYY"),
            time = formatUnixTime(response.body()!!.dt.toString(), "hh:mm a"),
            id = response.body()!!.id
        )
    }.flowOn(Dispatchers.IO)
}