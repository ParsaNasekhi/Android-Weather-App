package com.parsanasekhi.androidweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.parsanasekhi.androidweatherapp.ui.screens.bookmark.BookmarkScreen
import com.parsanasekhi.androidweatherapp.ui.screens.home.HomeScreen
import com.parsanasekhi.androidweatherapp.ui.theme.AndroidWeatherAppTheme
import com.parsanasekhi.androidweatherapp.ui.widgets.BottomWeatherAppBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidWeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {

    val pagerState = rememberPagerState { 2 }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.app_background),
                contentDescription = "App Background",
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                    if (page == 0) HomeScreen()
                    else BookmarkScreen()
                }
                BottomWeatherAppBar()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Gray
    ) {
        MainScreen()
    }
}