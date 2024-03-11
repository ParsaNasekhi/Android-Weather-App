package com.parsanasekhi.androidweatherapp.ui.screens.bookmark

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.parsanasekhi.androidweatherapp.data.City
import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.ui.MainScreen
import com.parsanasekhi.androidweatherapp.ui.theme.Black
import com.parsanasekhi.androidweatherapp.ui.theme.Orange
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentBlack
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentOrange
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentWhite
import com.parsanasekhi.androidweatherapp.ui.theme.White
import com.parsanasekhi.androidweatherapp.utills.BottomAppBarHeight
import com.parsanasekhi.androidweatherapp.utills.cityFromBookmarkScreen
import com.parsanasekhi.androidweatherapp.utills.removeCityEvent
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
    val dialogState = remember {
        mutableStateOf(false)
    }

    var cityToDelete: City? = null

    ShowAlertDialog(dialogState) {
        bookmarkViewModel.unbookmarkCity(cityToDelete!!)
        removeCityEvent.value = true
        cityToDelete = null
        dialogState.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BookmarkedListView(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            citiesWeather,
            onDelete = { city ->
                cityToDelete = city
                dialogState.value = true
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
fun ShowAlertDialog(
    dialogState: MutableState<Boolean>,
    onConfirm: () -> Unit
) {
    if (dialogState.value) {
        AlertDialog(
            shape = RoundedCornerShape(16.dp),
            containerColor = Black.copy(0.9f),
            onDismissRequest = {
                dialogState.value = false
            },
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Weather Icon",
                        tint = White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                    },
                    colors = ButtonColors(
                        containerColor = TransparentBlack,
                        contentColor = White,
                        disabledContainerColor = TransparentBlack,
                        disabledContentColor = Orange
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Remove this city",
                        fontSize = 16.sp
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dialogState.value = false
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
                        text = "Cancel",
                        fontSize = 16.sp
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
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            bottom = BottomAppBarHeight.dp
        )
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
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = cityWeather.cityName,
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    text = cityWeather.description,
                    color = TransparentWhite,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                )
            }
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = cityWeather.temp,
                    color = Orange,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
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