package com.parsanasekhi.androidweatherapp.ui.screens.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.data.ForecastWeather
import com.parsanasekhi.androidweatherapp.db.remote.ApiUrl
import com.parsanasekhi.androidweatherapp.ui.MainScreen
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentWhite
import com.parsanasekhi.androidweatherapp.ui.theme.White
import com.parsanasekhi.androidweatherapp.ui.theme.Yellow

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val currentWeather = homeViewModel.currentWeather.collectAsState()
    val forecastWeather = homeViewModel.forecastWeather.collectAsState()

    val cityName = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), text = cityName
        ) { newText ->
            cityName.value = newText
            homeViewModel.getCurrentWeather(cityName.value)
            homeViewModel.getForecastWeather(cityName.value, "5")
        }
        Spacer(modifier = Modifier.height(16.dp))
        HomePager(modifier = Modifier.fillMaxWidth(), currentWeather)
        WeekWeatherView(modifier = Modifier.fillMaxWidth(), forecastWeather)
        MoreInfo(modifier = Modifier.fillMaxWidth(), currentWeather, homeViewModel)
    }
}

@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier, text: State<String>, onSearch: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text.value,
        onValueChange = { newValue ->
            onSearch(newValue)
        },
        label = {
            Text(
                text = "Enter a city name", color = TransparentWhite
            )
        },
        maxLines = 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Yellow,
            focusedTextColor = White,
            disabledBorderColor = TransparentWhite,
            disabledTextColor = TransparentWhite
        ),
    )
}

@Composable
private fun MoreInfo(
    modifier: Modifier = Modifier,
    currentWeather: State<CurrentWeather>,
    homeViewModel: HomeViewModel
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MoreInfoItem(
            title = "wind", value = currentWeather.value.windSpeed
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(40.dp)
                .width(1.dp)
                .background(color = TransparentWhite)
        )
        MoreInfoItem(
            title = "humidity", value = currentWeather.value.humidity
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(40.dp)
                .width(1.dp)
                .background(color = TransparentWhite)
        )
        MoreInfoItem(title = "sunrise", value = currentWeather.value.sunrise.ifEmpty { "" })
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(40.dp)
                .width(1.dp)
                .background(color = TransparentWhite)
        )
        MoreInfoItem(title = "sunset", value = currentWeather.value.sunset.ifEmpty { "" })
    }
}

@Composable
private fun MoreInfoItem(
    title: String, value: String, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            color = Yellow,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = White,
            fontSize = 16.sp,
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun WeekWeatherView(
    modifier: Modifier = Modifier, forecastWeather: State<List<ForecastWeather.Detail>>
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(color = TransparentWhite)
        )
        LazyRow(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .horizontalScroll(scrollState)
                .width(screenWidth.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
                items(forecastWeather.value.size) { dayNum ->
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (forecastWeather.value.isNotEmpty()) forecastWeather.value[dayNum].date else "",
                        color = White,
                        fontSize = 12.sp,
                    )
                    Text(
                        text = if (forecastWeather.value.isNotEmpty()) forecastWeather.value[dayNum].time else "",
                        color = TransparentWhite,
                        fontSize = 12.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (forecastWeather.value[dayNum].icon.isNotEmpty())
                        GlideImage(
                            model = "${ApiUrl.LoadImageUrl}${forecastWeather.value[dayNum].icon}.png",
                            contentDescription = "Forecast Weather Icon",
                            modifier = Modifier.size(48.dp),
                        )
                    else
                    Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Weather Icon",
                            tint = White,
                            modifier = Modifier.size(48.dp)
                        )
                    Text(
                        text = if (forecastWeather.value.isNotEmpty()) forecastWeather.value[dayNum].temp else "",
                        color = White,
                        fontSize = 16.sp,
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(color = TransparentWhite)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomePager(modifier: Modifier = Modifier, currentWeather: State<CurrentWeather>) {

    val pagerState = rememberPagerState { 2 }

    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(), state = pagerState
        ) { page ->
            if (page == 0) MainWeatherInfoView(modifier = Modifier.fillMaxWidth(), currentWeather)
            else MainWeatherInfoView(currentWeather = currentWeather)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) White else TransparentWhite
                val width = if (pagerState.currentPage == iteration) 32.dp else 16.dp
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .height(8.dp)
                        .width(width)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MainWeatherInfoView(
    modifier: Modifier = Modifier, currentWeather: State<CurrentWeather>
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = currentWeather.value.name,
            color = White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = currentWeather.value.description,
            color = TransparentWhite,
            fontSize = 24.sp,
        )
        Spacer(modifier = Modifier.height(14.dp))
        if (currentWeather.value.icon.isNotEmpty())
            GlideImage(
                model = "${ApiUrl.LoadImageUrl}${currentWeather.value.icon}.png",
                contentDescription = "Current Weather Icon",
                modifier = Modifier.size(100.dp),
            )
        else Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "Weather Icon",
            tint = TransparentWhite,
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = currentWeather.value.temp,
            color = White,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "max",
                    color = TransparentWhite,
                    fontSize = 16.sp,
                )
                Text(
                    text = currentWeather.value.maxTemp,
                    color = White,
                    fontSize = 16.sp,
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(40.dp)
                    .width(1.dp)
                    .background(color = TransparentWhite)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "min",
                    color = TransparentWhite,
                    fontSize = 16.sp,
                )
                Text(
                    text = currentWeather.value.minTemp,
                    color = White,
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.Gray
    ) {
        MainScreen(pageCount = 1) {
            HomeScreen()
        }
    }
}