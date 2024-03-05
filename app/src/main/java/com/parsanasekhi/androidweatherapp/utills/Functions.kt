package com.parsanasekhi.androidweatherapp.utills

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatUnixTime(unixTime: String, pattern: String): String {
    val date = Date(unixTime.toLong() * 1000)
    val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
    return sdf.format(date)
}