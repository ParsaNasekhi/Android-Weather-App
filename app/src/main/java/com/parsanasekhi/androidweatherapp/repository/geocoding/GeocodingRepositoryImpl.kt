package com.parsanasekhi.androidweatherapp.repository.geocoding

import com.parsanasekhi.androidweatherapp.db.remote.geocoding.GeocodingApiService
import com.parsanasekhi.androidweatherapp.db.remote.geocoding.GeocodingResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class GeocodingRepositoryImpl @Inject constructor(private val geocodingApiService: GeocodingApiService) : GeocodingRepository {
    override suspend fun getLocation(cityName: String): Flow<Response<GeocodingResponse>> = flow {
        emit(geocodingApiService.getLocation(cityName))
    }.flowOn(Dispatchers.IO)

}