package com.parsanasekhi.androidweatherapp.utills

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertUnixToAmPm(unixTimestamp: String): String {
//    val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
//    utcFormat.timeZone = TimeZone.getTimeZone("UTC")
//    val date = utcFormat.parse(utcTime)
//    val amPmFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
//    return amPmFormat.format(date!!)
    val date = Date(unixTimestamp.toLong() * 1000) // Convert Unix timestamp to milliseconds
    val amPmFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return amPmFormat.format(date)
}