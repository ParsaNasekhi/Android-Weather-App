package com.parsanasekhi.androidweatherapp.ui.screens.bookmark

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.parsanasekhi.androidweatherapp.data.City
import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.ui.MainScreen
import com.parsanasekhi.androidweatherapp.ui.theme.Orange
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentBlack
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentWhite
import com.parsanasekhi.androidweatherapp.ui.theme.White
import com.parsanasekhi.androidweatherapp.utills.cityFromBookmarkScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkScreen(
    pagerState: PagerState,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel()
) {

    remember {
        bookmarkViewModel.getBookmarkedCitiesWeather()
    }

    val citiesWeather = bookmarkViewModel.citiesWeather

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BookmarkedListView(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            citiesWeather,
            onDelete = { city ->
                bookmarkViewModel.unbookmarkCity(city)
            },
            onClick = { city ->
                cityFromBookmarkScreen.value = city
                scope.launch {
                    pagerState.animateScrollToPage(
                        page = 0,
                        animationSpec = tween(durationMillis = 1000)
                    )
                }
            }
        )
    }
}

@Composable
fun BookmarkedListView(
    modifier: Modifier = Modifier,
    citiesWeather: SnapshotStateList<CurrentWeather>,
    onDelete: (City) -> Unit,
    onClick: (City) -> Unit
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(citiesWeather.size) { index ->

            val alpha = remember {
                mutableFloatStateOf(0f)
            }

            BookmarkedView(
                modifier = Modifier
                    .alpha(
                        animateFloatAsState(
                            targetValue = alpha.value,
                            animationSpec = tween(durationMillis = 2000),
                            label = "Fade In Animation For BookmarkedView"
                        ).value
                    )
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(90.dp),
                cityWeather = citiesWeather[index],
                onDelete = onDelete,
                onClick = onClick
            )

            LaunchedEffect(key1 = null) {
                delay(10)
                alpha.value = 1f
            }
        }
    }


}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun BookmarkedView(
    modifier: Modifier = Modifier,
    cityWeather: CurrentWeather,
    onDelete: (City) -> Unit,
    onClick: (City) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = TransparentBlack),
        modifier = modifier
            .combinedClickable(
                onLongClick = {
                    onDelete(City(cityWeather.cityName, cityWeather.cityId!!))
                },
                onClick = {
                    onClick(City(cityWeather.cityName, cityWeather.cityId!!))
                }
            ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                GlideImage(
                    model = cityWeather.icon,
                    contentDescription = "City Weather Icon",
                    modifier = Modifier.size(64.dp),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = cityWeather.cityName,
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = cityWeather.description,
                    color = TransparentWhite,
                    fontSize = 16.sp
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = cityWeather.temp,
                    color = Orange,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun BookmarkScreenPreview() {

    val pagerState = rememberPagerState {
        1
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.Gray
    ) {
        MainScreen(pagerState) {
            BookmarkScreen(pagerState)
        }
    }
}