package com.parsanasekhi.androidweatherapp.ui.screens.bookmark

import androidx.lifecycle.ViewModel
import com.parsanasekhi.androidweatherapp.data.City
import com.parsanasekhi.androidweatherapp.repository.bookmarked_city.BookmarkedCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val bookmarkedCityRepository: BookmarkedCityRepository) :
    ViewModel() {

    fun unbookmarkCity(cityId: Int) {

    }

    fun getBookmarkedCities(): List<City> {
        return listOf()
    }

}