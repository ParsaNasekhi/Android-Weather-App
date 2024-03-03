package com.parsanasekhi.androidweatherapp.repository.geocoding

import com.parsanasekhi.androidweatherapp.db.remote.geocoding.GeocodingResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface GeocodingRepository {

    suspend fun getLocation(
        cityName: String
    ): Flow<Response<GeocodingResponse>>

}