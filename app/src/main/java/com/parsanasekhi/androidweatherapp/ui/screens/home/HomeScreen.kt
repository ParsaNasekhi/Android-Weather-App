package com.parsanasekhi.androidweatherapp.ui.screens.home

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.parsanasekhi.androidweatherapp.config.checkLocationPermission
import com.parsanasekhi.androidweatherapp.config.currentLatitude
import com.parsanasekhi.androidweatherapp.config.currentLongitude
import com.parsanasekhi.androidweatherapp.config.isLocationProviderEnabled
import com.parsanasekhi.androidweatherapp.config.locationListener
import com.parsanasekhi.androidweatherapp.config.runLocationListener
import com.parsanasekhi.androidweatherapp.data.City
import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.data.ForecastWeather
import com.parsanasekhi.androidweatherapp.data.Location
import com.parsanasekhi.androidweatherapp.ui.MainScreen
import com.parsanasekhi.androidweatherapp.ui.theme.Orange
import com.parsanasekhi.androidweatherapp.ui.theme.Red
import com.parsanasekhi.androidweatherapp.ui.theme.Transparent
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentBlack
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentOrange
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentWhite
import com.parsanasekhi.androidweatherapp.ui.theme.White
import com.parsanasekhi.androidweatherapp.ui.widgets.LoadingCircleView
import com.parsanasekhi.androidweatherapp.utills.BottomAppBarHeight
import com.parsanasekhi.androidweatherapp.utills.LoadState
import com.parsanasekhi.androidweatherapp.utills.check
import com.parsanasekhi.androidweatherapp.utills.cityFromBookmarkScreen
import com.parsanasekhi.androidweatherapp.utills.removeCityEvent
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val currentWeather = homeViewModel.currentWeather.collectAsState()
    val currentWeatherLoadState = homeViewModel.currentWeatherLoadState.collectAsState()

    val forecastWeather = homeViewModel.forecastWeather.collectAsState()
    val forecastWeatherLoadState = homeViewModel.forecastWeatherLoadState.collectAsState()

    val isCityBookmarked = homeViewModel.isCityBookmarked.collectAsState()

    val clickedForecastItem = remember {
        mutableStateOf<Int?>(null)
    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val locationManager by remember {
        mutableStateOf(context.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
    }

    if (cityFromBookmarkScreen.value != null) {
        homeViewModel.getWeatherByCityId(cityFromBookmarkScreen.value!!.id)
        cityFromBookmarkScreen.value = null
    }

    if (removeCityEvent.value) {
        removeCityEvent.value = false
        coroutineScope.launch {
            if (currentWeather.value.cityId != null)
                homeViewModel.checkIsCityBookmarked(currentWeather.value.cityId!!)
        }
    }

    if (isLocationProviderEnabled.value != null) {
        if (!isLocationProviderEnabled.value!!) {
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(settingsIntent)
            isLocationProviderEnabled.value = null
        }
    }

    if (currentLatitude.value.isNotEmpty() && currentLongitude.value.isNotEmpty()) {
        homeViewModel.getWeatherByCityLocation(
            Location(
                lat = currentLatitude.value,
                lon = currentLongitude.value
            )
        )
        currentLatitude.value = ""
        currentLongitude.value = ""
        locationManager.removeUpdates(locationListener)
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
                    .padding(horizontal = 16.dp),
                locationManager = locationManager
            ) { newText ->
                clickedForecastItem.value = null
                homeViewModel.getCurrentWeather(newText)
                homeViewModel.getForecastWeather(newText, "5")
            }
            Spacer(modifier = Modifier.height(16.dp))
            HomePagerView(
                modifier = Modifier.fillMaxWidth(),
                currentWeather = currentWeather,
                forecastWeather = forecastWeather,
                clickedForecastItem = clickedForecastItem,
                isCityBookmarked = isCityBookmarked,
                currentWeatherLoadState = currentWeatherLoadState
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
                clickedForecastItem = clickedForecastItem,
                currentWeatherLoadState = currentWeatherLoadState,
                forecastWeatherLoadState = forecastWeatherLoadState
            ) { dayNum ->
                clickedForecastItem.value = dayNum
            }
            MoreInfoView(
                modifier = Modifier.fillMaxWidth(),
                currentWeather = currentWeather,
                forecastWeather = forecastWeather,
                clickedForecastItem = clickedForecastItem,
                currentWeatherLoadState = currentWeatherLoadState
            )
        }
    }
}

@Composable
private fun SearchCityView(
    modifier: Modifier = Modifier,
    locationManager: LocationManager,
    onSearch: (String) -> Unit
) {

    val context = LocalContext.current

    val text = remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = modifier,
        value = text.value,
        onValueChange = { newValue ->
            text.value = newValue
        },
        label = {
            Text(
                text = "Enter a city name",
                color = TransparentWhite
            )
        },
        maxLines = 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Orange,
            focusedTextColor = White,
            disabledBorderColor = TransparentWhite,
            disabledTextColor = TransparentWhite
        ),
        trailingIcon = {
            Row {
                IconButton(
                    onClick = {
                        onSearch(text.value)
                    }
                ) {
                    Image(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        modifier = Modifier
                            .size(30.dp),
                        colorFilter = ColorFilter.tint(White),
                        contentScale = ContentScale.FillHeight
                    )
                }
                IconButton(onClick = {
                    checkLocationPermission(context) {
                        runLocationListener(locationManager)
                    }
                }) {
                    Image(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location Icon",
                        modifier = Modifier
                            .size(30.dp),
                        colorFilter = ColorFilter.tint(White),
                        contentScale = ContentScale.FillHeight
                    )
                }
            }
        }
    )
}

@Composable
private fun MoreInfoView(
    modifier: Modifier = Modifier,
    currentWeather: State<CurrentWeather>,
    forecastWeather: State<List<ForecastWeather.Detail>>,
    clickedForecastItem: MutableState<Int?>,
    currentWeatherLoadState: State<LoadState>
) {
    Row(
        modifier = currentWeatherLoadState.value.check(
            successContent = modifier,
            loadingContent = modifier.shimmer(),
            elseContent = modifier.alpha(0.5f)
        )!!,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MoreInfoItem(
            title = "wind",
            value = currentWeatherLoadState.value.check(
                successContent = if (clickedForecastItem.value == null)
                    currentWeather.value.windSpeed
                else
                    forecastWeather.value[clickedForecastItem.value!!].windSpeed,
                elseContent = ""
            )!!
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
            value = currentWeatherLoadState.value.check(
                successContent = if (clickedForecastItem.value == null)
                    currentWeather.value.humidity
                else
                    forecastWeather.value[clickedForecastItem.value!!].humidity,
                elseContent = ""
            )!!
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
            value = currentWeatherLoadState.value.check(
                successContent = if (clickedForecastItem.value == null)
                    currentWeather.value.sunrise
                else
                    forecastWeather.value[clickedForecastItem.value!!].sunrise,
                elseContent = ""
            )!!
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
            value = currentWeatherLoadState.value.check(
                successContent = if (clickedForecastItem.value == null)
                    currentWeather.value.sunset
                else
                    forecastWeather.value[clickedForecastItem.value!!].sunset,
                elseContent = ""
            )!!
        )
    }
}

@Composable
private fun MoreInfoItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
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
    currentWeatherLoadState: State<LoadState>,
    forecastWeatherLoadState: State<LoadState>,
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
                Column(
                    modifier = Modifier
                        .clickable {
                            onItemClicked(null)
                        }
                        .background(
                            color =
                            if (clickedForecastItem.value == null) TransparentOrange
                            else Transparent
                        )
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentWeatherLoadState.value.check(
                            successContent = "Today",
                            elseContent = "Text"
                        )!!,
                        color = currentWeatherLoadState.value.check(
                            successContent = White,
                            elseContent = Transparent
                        )!!,
                        fontSize = 12.sp,
                    )
                    Text(
                        text = currentWeatherLoadState.value.check(
                            successContent = "Right Now",
                            elseContent = "Text"
                        )!!,
                        color = currentWeatherLoadState.value.check(
                            successContent = TransparentWhite,
                            elseContent = Transparent
                        )!!,
                        fontSize = 12.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    when (currentWeatherLoadState.value) {
                        LoadState.LOADING ->
                            LoadingCircleView(
                                display = true,
                                modifier = Modifier.size(48.dp)
                            )

                        LoadState.SUCCESS ->
                            GlideImage(
                                model = currentWeather.value.icon,
                                contentDescription = "Forecast Weather Icon",
                                modifier = Modifier.size(48.dp),
                            )

                        else ->
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Weather Icon",
                                tint = TransparentBlack,
                                modifier = Modifier.size(48.dp)
                            )
                    }
                    Text(
                        text = currentWeatherLoadState.value.check(
                            successContent = currentWeather.value.temp,
                            elseContent = "Text"
                        )!!,
                        color = currentWeatherLoadState.value.check(
                            successContent = White,
                            elseContent = Transparent
                        )!!,
                        fontSize = 16.sp,
                    )
                }
            }
            items(forecastWeather.value.size) { dayNum ->
                ForecastWeatherItemView(
                    dayNum = dayNum,
                    clickedForecastItem = clickedForecastItem,
                    forecastWeather = forecastWeather,
                    onItemClicked = onItemClicked,
                    forecastWeatherLoadState = forecastWeatherLoadState
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
    onItemClicked: (Int) -> Unit,
    forecastWeatherLoadState: State<LoadState>
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
            text = forecastWeatherLoadState.value.check(
                successContent = forecastWeather.value[dayNum].date,
                elseContent = "Text"
            )!!,
            color = forecastWeatherLoadState.value.check(
                successContent = White,
                elseContent = Transparent
            )!!,
            fontSize = 12.sp,
        )
        Text(
            text = forecastWeatherLoadState.value.check(
                successContent = forecastWeather.value[dayNum].time,
                elseContent = "Text"
            )!!,
            color = forecastWeatherLoadState.value.check(
                successContent = TransparentWhite,
                elseContent = Transparent
            )!!,
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        when (forecastWeatherLoadState.value) {
            LoadState.LOADING ->
                LoadingCircleView(
                    display = true,
                    modifier = Modifier.size(48.dp)
                )

            LoadState.SUCCESS ->
                GlideImage(
                    model = forecastWeather.value[dayNum].icon,
                    contentDescription = "Forecast Weather Icon",
                    modifier = Modifier.size(48.dp),
                )

            else ->
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Weather Icon",
                    tint = TransparentBlack,
                    modifier = Modifier.size(48.dp)
                )
        }
        Text(
            text = forecastWeatherLoadState.value.check(
                successContent = forecastWeather.value[dayNum].temp,
                elseContent = "Text"
            )!!,
            color = forecastWeatherLoadState.value.check(
                successContent = White,
                elseContent = Transparent
            )!!,
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
    currentWeatherLoadState: State<LoadState>,
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
                    clickedForecastItem = clickedForecastItem,
                    currentWeatherLoadState = currentWeatherLoadState
                )
            else
                AboutCityPageView(
                    modifier = Modifier
                        .fillMaxSize(),
                    currentWeather = currentWeather,
                    onBookmark = onBookmark,
                    isCityBookmarked = isCityBookmarked,
                    currentWeatherLoadState = currentWeatherLoadState
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
    isCityBookmarked: State<Boolean>,
    currentWeatherLoadState: State<LoadState>
) {

    val spacerHeight = 16.dp

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = currentWeatherLoadState.value.check(
                successContent = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f),
                elseContent = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f)
                    .alpha(0.5f),
                loadingContent = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f)
                    .shimmer()
            )!!,
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
                    value = currentWeatherLoadState.value.check(
                        successContent = currentWeather.value.cityName,
                        elseContent = ""
                    )!!
                )
                Spacer(modifier = Modifier.height(spacerHeight))
                AboutCityItemView(
                    title = "Date",
                    value = currentWeatherLoadState.value.check(
                        successContent = currentWeather.value.date,
                        elseContent = ""
                    )!!
                )
                Spacer(modifier = Modifier.height(spacerHeight))
                AboutCityItemView(
                    title = "Latitude",
                    value = currentWeatherLoadState.value.check(
                        successContent = currentWeather.value.location.lat,
                        elseContent = ""
                    )!!
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
                    value = currentWeatherLoadState.value.check(
                        successContent = currentWeather.value.country,
                        elseContent = ""
                    )!!
                )
                Spacer(modifier = Modifier.height(spacerHeight))
                AboutCityItemView(
                    title = "Time",
                    value = currentWeatherLoadState.value.check(
                        successContent = currentWeather.value.time,
                        elseContent = ""
                    )!!
                )
                Spacer(modifier = Modifier.height(spacerHeight))
                AboutCityItemView(
                    title = "Longitude",
                    value = currentWeatherLoadState.value.check(
                        successContent = currentWeather.value.location.lon,
                        elseContent = ""
                    )!!
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
                disabledContentColor = TransparentOrange
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            enabled = currentWeatherLoadState.value.check(
                successContent = true,
                elseContent = false
            )!!
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
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = value,
            color = White,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CurrentWeatherPageView(
    modifier: Modifier = Modifier,
    currentWeather: State<CurrentWeather>,
    forecastWeather: State<List<ForecastWeather.Detail>>,
    clickedForecastItem: MutableState<Int?>,
    currentWeatherLoadState: State<LoadState>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = currentWeatherLoadState.value.check(
                successContent = currentWeather.value.cityName,
                loadingContent = "Transparent Text",
                emptyContent = "Not Found!",
                errorContent = "Connection Lost!",
                elseContent = null
            )!!,
            color = currentWeatherLoadState.value.check(
                successContent = White,
                loadingContent = Transparent,
                emptyContent = TransparentWhite,
                errorContent = Red,
                elseContent = null
            )!!,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = currentWeatherLoadState.value.check(
                successContent = if (clickedForecastItem.value == null)
                    currentWeather.value.description
                else
                    forecastWeather.value[clickedForecastItem.value!!].description,
                elseContent = "Transparent Text"
            )!!,
            color = currentWeatherLoadState.value.check(
                successContent = TransparentWhite,
                elseContent = Transparent
            )!!,
            fontSize = 24.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(14.dp))
        when (currentWeatherLoadState.value) {
            LoadState.LOADING ->
                LoadingCircleView(
                    display = true,
                    modifier = Modifier.size(100.dp)
                )

            LoadState.ERROR, LoadState.EMPTY ->
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Weather Icon",
                    tint = Red,
                    modifier = Modifier
                        .size(100.dp)
                        .shimmer()
                )

            else ->
                GlideImage(
                    model =
                    if (clickedForecastItem.value == null)
                        currentWeather.value.icon
                    else
                        forecastWeather.value[clickedForecastItem.value!!].icon,
                    contentDescription = "Current Weather Icon",
                    modifier = Modifier.size(100.dp),
                )
        }
        Text(
            text = currentWeatherLoadState.value.check(
                successContent = if (clickedForecastItem.value == null)
                    currentWeather.value.temp
                else
                    forecastWeather.value[clickedForecastItem.value!!].temp,
                elseContent = "Transparent Text"
            )!!,
            color = currentWeatherLoadState.value.check(
                successContent = White,
                elseContent = Transparent
            )!!,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = currentWeatherLoadState.value.check(
                successContent = Modifier,
                loadingContent = Modifier.shimmer(),
                elseContent = Modifier.alpha(0.5f)
            )!!
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
                    text = currentWeatherLoadState.value.check(
                        successContent =
                        if (clickedForecastItem.value == null)
                            currentWeather.value.maxTemp
                        else
                            forecastWeather.value[clickedForecastItem.value!!].maxTemp,
                        elseContent = "Text"
                    )!!,
                    color = currentWeatherLoadState.value.check(
                        successContent = White,
                        elseContent = Transparent
                    )!!,
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
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "min",
                    color = TransparentWhite,
                    fontSize = 16.sp,
                )
                Text(
                    text = currentWeatherLoadState.value.check(
                        successContent = if (clickedForecastItem.value == null)
                            currentWeather.value.minTemp
                        else
                            forecastWeather.value[clickedForecastItem.value!!].minTemp,
                        elseContent = "Text"
                    )!!,
                    color = currentWeatherLoadState.value.check(
                        successContent = White,
                        elseContent = Transparent
                    )!!,
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

    val pagerState = rememberPagerState { 1 }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Gray
    ) {
        MainScreen(pagerState) {
            HomeScreen()
        }
    }
}