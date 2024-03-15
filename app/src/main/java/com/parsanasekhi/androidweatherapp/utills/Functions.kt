package com.parsanasekhi.androidweatherapp.utills

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatUnixTime(unixTime: String, pattern: String): String {
    val date = Date(unixTime.toLong() * 1000)
    val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
    return sdf.format(date)
}

fun <T> LoadState.check(
    successContent: T?,
    loadingContent: T? = null,
    emptyContent: T? = null,
    errorContent: T? = null,
    elseContent: T?
): T? {
    return when {
        this == LoadState.SUCCESS && successContent != null -> {
            successContent
        }

        this == LoadState.LOADING && loadingContent != null -> {
            loadingContent
        }

        this == LoadState.EMPTY && emptyContent != null -> {
            emptyContent
        }

        this == LoadState.ERROR && errorContent != null -> {
            errorContent
        }

        else -> {
            elseContent
        }
    }
}