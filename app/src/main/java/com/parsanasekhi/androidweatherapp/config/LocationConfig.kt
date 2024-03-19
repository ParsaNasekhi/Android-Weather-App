package com.parsanasekhi.androidweatherapp.config

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat

val currentLatitude = mutableStateOf("")
val currentLongitude = mutableStateOf("")
val isLocationProviderEnabled = mutableStateOf<Boolean?>(null)

val locationListener: LocationListener = object : LocationListener {
    override fun onLocationChanged(location: Location) {
        currentLatitude.value = ""
        currentLatitude.value = location.latitude.toString()
        currentLongitude.value = ""
        currentLongitude.value = location.longitude.toString()
        Log.i("TestLog", "onLocationChanged: (${currentLatitude.value}, ${currentLongitude.value})")
    }

    override fun onProviderEnabled(provider: String) {
        isLocationProviderEnabled.value = true
        Log.i("TestLog", "onLocationEnabled: (${provider})")
    }

    override fun onProviderDisabled(provider: String) {
        isLocationProviderEnabled.value = false
        Log.i("TestLog", "onLocationDisabled: (${provider})")
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }
}

fun checkLocationPermission(context: Context, onAccept: () -> Unit) {
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1001
        )
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onAccept()
        }
    } else {
        onAccept()
    }
}

@SuppressLint("MissingPermission")
fun runLocationListener(locationManager: LocationManager) {
    locationManager.requestLocationUpdates(
        LocationManager.NETWORK_PROVIDER,
        0,
        0f,
        locationListener,
        null
    )
}