package com.parsanasekhi.androidweatherapp.ui.screens.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.parsanasekhi.androidweatherapp.data.City
import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.data.ForecastWeather
import com.parsanasekhi.androidweatherapp.ui.MainScreen
import com.parsanasekhi.androidweatherapp.ui.theme.Orange
import com.parsanasekhi.androidweatherapp.ui.theme.Transparent
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentBlack
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentOrange
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentWhite
import com.parsanasekhi.androidweatherapp.ui.theme.White
import com.parsanasekhi.androidweatherapp.utills.BottomAppBarHeight
import com.parsanasekhi.androidweatherapp.utills.EmptyCurrentWeather
import com.parsanasekhi.androidweatherapp.utills.cityFromBookmarkScreen
import com.parsanasekhi.androidweatherapp.utills.removeCityEvent
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val currentWeather = homeViewModel.currentWeather.collectAsState()
    val forecastWeather = homeViewModel.forecastWeather.collectAsState()
    val isCityBookmarked = homeViewModel.isCityBookmarked.collectAsState()

    val cityName = remember {
        mutableStateOf("")
    }
    val clickedForecastItem = remember {
        mutableStateOf<Int?>(null)
    }

    val coroutineScope = rememberCoroutineScope()

    if (cityFromBookmarkScreen.value != null) {
        homeViewModel.getWeatherByCityId(cityFromBookmarkScreen.value!!.id)
        cityFromBookmarkScreen.value = null
    }

    if (removeCityEvent.value) {
        removeCityEvent.value = false
        coroutineScope.launch {
            homeViewModel.checkIsCityBookmarked(currentWeather.value.cityId!!)
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = BottomAppBarHeight.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            SearchCityView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), text = cityName
            ) { newText ->
                clickedForecastItem.value = null
                cityName.value = newText
                homeViewModel.getCurrentWeather(cityName.value)
                homeViewModel.getForecastWeather(cityName.value, "5")
            }
            Spacer(modifier = Modifier.height(16.dp))
            HomePagerView(
                modifier = Modifier.fillMaxWidth(),
                currentWeather = currentWeather,
                forecastWeather = forecastWeather,
                clickedForecastItem = clickedForecastItem,
                isCityBookmarked = isCityBookmarked
            ) { city ->
                if (!isCityBookmarked.value)
                    homeViewModel.bookmarkCity(city)
                else
                    homeViewModel.unbookmarkCity(city)
            }
            ForecastWeatherListView(
                modifier = Modifier.fillMaxWidth(),
                currentWeather = currentWeather,
                forecastWeather = forecastWeather,
                clickedForecastItem = clickedForecastItem
            ) { dayNum ->
                clickedForecastItem.value = dayNum
            }
            MoreInfoView(
                modifier = Modifier.fillMaxWidth(),
                currentWeather = currentWeather,
                forecastWeather = forecastWeather,
                clickedForecastItem = clickedForecastItem,
                homeViewModel = homeViewModel
            )
        }
    }
}

@Composable
private fun SearchCityView(
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
            focusedBorderColor = Orange,
            focusedTextColor = White,
            disabledBorderColor = TransparentWhite,
            disabledTextColor = TransparentWhite
        ),
    )
}

@Composable
private fun MoreInfoView(
    modifier: Modifier = Modifier,
    currentWeather: State<CurrentWeather>,
    forecastWeather: State<List<ForecastWeather.Detail>>,
    clickedForecastItem: MutableState<Int?>,
    homeViewModel: HomeViewModel
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MoreInfoItem(
            title = "wind",
            value =
            if (clickedForecastItem.value == null)
                currentWeather.value.windSpeed
            else
                forecastWeather.value[clickedForecastItem.value!!].windSpeed
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(40.dp)
                .width(1.dp)
                .background(color = TransparentWhite)
        )
        MoreInfoItem(
            title = "humidity",
            value =
            if (clickedForecastItem.value == null)
                currentWeather.value.humidity
            else
                forecastWeather.value[clickedForecastItem.value!!].humidity
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(40.dp)
                .width(1.dp)
                .background(color = TransparentWhite)
        )
        MoreInfoItem(
            title = "sunrise",
            value =
            if (clickedForecastItem.value == null)
                currentWeather.value.sunrise
            else
                forecastWeather.value[clickedForecastItem.value!!].sunrise
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(40.dp)
                .width(1.dp)
                .background(color = TransparentWhite)
        )
        MoreInfoItem(
            title = "sunset",
            value =
            if (clickedForecastItem.value == null)
                currentWeather.value.sunset
            else
                forecastWeather.value[clickedForecastItem.value!!].sunset
        )
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
            color = Orange,
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
private fun ForecastWeatherListView(
    modifier: Modifier = Modifier,
    currentWeather: State<CurrentWeather>,
    forecastWeather: State<List<ForecastWeather.Detail>>,
    clickedForecastItem: MutableState<Int?>,
    onItemClicked: (Int?) -> Unit
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp

    Column(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(color = TransparentWhite)
        )
        LazyRow(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .width(screenWidth.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                if (currentWeather.value != EmptyCurrentWeather)
                    Column(
                        modifier = Modifier
                            .clickable {
                                onItemClicked(null)
                            }
                            .background(color = if (clickedForecastItem.value == null) TransparentOrange else Transparent)
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Today",
                            color = White,
                            fontSize = 12.sp,
                        )
                        Text(
                            text = "Right Now",
                            color = TransparentWhite,
                            fontSize = 12.sp,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        GlideImage(
                            model = currentWeather.value.icon,
                            contentDescription = "Forecast Weather Icon",
                            modifier = Modifier.size(48.dp),
                        )
                        Text(
                            text = currentWeather.value.temp,
                            color = White,
                            fontSize = 16.sp,
                        )
                    }
            }
            items(forecastWeather.value.size) { dayNum ->
                ForecastWeatherItemView(
                    dayNum = dayNum,
                    clickedForecastItem = clickedForecastItem,
                    forecastWeather = forecastWeather,
                    onItemClicked = onItemClicked
                )
            }
        }
        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(color = TransparentWhite)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ForecastWeatherItemView(
    dayNum: Int,
    clickedForecastItem: MutableState<Int?>,
    forecastWeather: State<List<ForecastWeather.Detail>>,
    onItemClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable {
                onItemClicked(dayNum)
            }
            .background(color = if (clickedForecastItem.value == dayNum) TransparentOrange else Transparent)
            .padding(horizontal = 8.dp, vertical = 8.dp),
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
                model = forecastWeather.value[dayNum].icon,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomePagerView(
    modifier: Modifier = Modifier,
    currentWeather: State<CurrentWeather>,
    forecastWeather: State<List<ForecastWeather.Detail>>,
    clickedForecastItem: MutableState<Int?>,
    isCityBookmarked: State<Boolean>,
    onBookmark: (City) -> Unit
) {

    val pagerState = rememberPagerState { 2 }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            state = pagerState
        ) { page ->
            if (page == 0)
                CurrentWeatherPageView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    currentWeather = currentWeather,
                    forecastWeather = forecastWeather,
                    clickedForecastItem = clickedForecastItem
                )
            else
                AboutCityPageView(
                    modifier = Modifier
                        .fillMaxSize(),
                    currentWeather = currentWeather,
                    onBookmark = onBookmark,
                    isCityBookmarked = isCityBookmarked
                )
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
                val isInThisPage = pagerState.currentPage == iteration
                val color = if (isInThisPage) White else TransparentWhite
                val width = if (isInThisPage) 32.dp else 16.dp
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            animateColorAsState(
                                targetValue = color,
                                animationSpec = tween(durationMillis = 500),
                                label = "Home Pager Indicator Color Animation"
                            ).value
                        )
                        .height(8.dp)
                        .width(
                            animateDpAsState(
                                targetValue = width,
                                animationSpec = tween(durationMillis = 500),
                                label = "Home Pager Indicator Width Animation"
                            ).value
                        )
                )
            }
        }
    }
}

@Composable
fun AboutCityPageView(
    modifier: Modifier = Modifier,
    currentWeather: State<CurrentWeather>,
    onBookmark: (City) -> Unit,
    isCityBookmarked: State<Boolean>
) {

    val spacerHeight = 16.dp

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                AboutCityItemView(
                    title = "City",
                    value = currentWeather.value.cityName
                )
                Spacer(modifier = Modifier.height(spacerHeight))
                AboutCityItemView(
                    title = "Date",
                    value = currentWeather.value.date
                )
                Spacer(modifier = Modifier.height(spacerHeight))
                AboutCityItemView(
                    title = "Latitude",
                    value = currentWeather.value.location.lat
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                AboutCityItemView(
                    title = "Country",
                    value = currentWeather.value.country
                )
                Spacer(modifier = Modifier.height(spacerHeight))
                AboutCityItemView(
                    title = "Time",
                    value = currentWeather.value.time
                )
                Spacer(modifier = Modifier.height(spacerHeight))
                AboutCityItemView(
                    title = "Longitude",
                    value = currentWeather.value.location.lon
                )
            }

        }
        TextButton(
            onClick = {
                if (currentWeather.value.cityId != null)
                    onBookmark(
                        City(
                            currentWeather.value.cityName,
                            currentWeather.value.cityId!!
                        )
                    )
            },
            colors = ButtonColors(
                containerColor = TransparentOrange,
                contentColor = Orange,
                disabledContainerColor = TransparentBlack,
                disabledContentColor = Orange
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text =
                if (!isCityBookmarked.value)
                    "bookmark city"
                else
                    "unbookmark city",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun AboutCityItemView(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "$title:",
            color = TransparentWhite,
            fontSize = 16.sp
        )
        Text(
            text = value,
            color = White,
            fontSize = 16.sp
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CurrentWeatherPageView(
    modifier: Modifier = Modifier,
    currentWeather: State<CurrentWeather>,
    forecastWeather: State<List<ForecastWeather.Detail>>,
    clickedForecastItem: MutableState<Int?>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = currentWeather.value.cityName,
            color = White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text =
            if (clickedForecastItem.value == null)
                currentWeather.value.description
            else
                forecastWeather.value[clickedForecastItem.value!!].description,
            color = TransparentWhite,
            fontSize = 24.sp,
        )
        Spacer(modifier = Modifier.height(14.dp))
        if (currentWeather.value.icon.isNotEmpty())
            GlideImage(
                model =
                if (clickedForecastItem.value == null)
                    currentWeather.value.icon
                else
                    forecastWeather.value[clickedForecastItem.value!!].icon,
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
            text =
            if (clickedForecastItem.value == null)
                currentWeather.value.temp
            else
                forecastWeather.value[clickedForecastItem.value!!].temp,
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
                    text =
                    if (clickedForecastItem.value == null)
                        currentWeather.value.maxTemp
                    else
                        forecastWeather.value[clickedForecastItem.value!!].maxTemp,
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
                    text =
                    if (clickedForecastItem.value == null)
                        currentWeather.value.minTemp
                    else
                        forecastWeather.value[clickedForecastItem.value!!].minTemp,
                    color = White,
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {

    val pagerState = rememberPagerState {
        1
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.Gray
    ) {
        MainScreen(pagerState) {
            HomeScreen()
        }
    }
}