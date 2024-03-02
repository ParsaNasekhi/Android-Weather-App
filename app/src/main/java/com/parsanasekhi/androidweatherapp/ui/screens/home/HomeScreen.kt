package com.parsanasekhi.androidweatherapp.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parsanasekhi.androidweatherapp.ui.MainScreen
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentWhite
import com.parsanasekhi.androidweatherapp.ui.theme.White
import com.parsanasekhi.androidweatherapp.ui.theme.Yellow

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        HomePager(modifier = Modifier.fillMaxWidth())
        WeekWeatherView(modifier = Modifier.fillMaxWidth())
        MoreInfo(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun SearchTextField(modifier: Modifier = Modifier) {
    OutlinedTextField(
        modifier = modifier,
        value = "",
        onValueChange = {},
        label = {
            Text(
                text = "Enter a city name",
                color = TransparentWhite
            )
        }
    )
}

@Composable
private fun MoreInfo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MoreInfoItem(
            text = "6:29 AM"
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(40.dp)
                .width(1.dp)
                .background(color = TransparentWhite)
        )
        MoreInfoItem(
            text = "6:29 AM"
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(40.dp)
                .width(1.dp)
                .background(color = TransparentWhite)
        )
        MoreInfoItem(
            text = "6:29 AM"
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(40.dp)
                .width(1.dp)
                .background(color = TransparentWhite)
        )
        MoreInfoItem(
            text = "6:29 AM"
        )
    }
}

@Composable
private fun MoreInfoItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "sunrise",
            color = Yellow,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            color = White,
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun WeekWeatherView(modifier: Modifier = Modifier) {
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
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(6) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aug 23",
                        color = TransparentWhite,
                        fontSize = 12.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Weather Icon",
                        tint = White,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "31",
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
private fun HomePager(modifier: Modifier = Modifier) {

    val pagerState = rememberPagerState { 2 }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState
        ) { page ->
            if (page == 0) MainWeatherInfoView(modifier = Modifier.fillMaxWidth())
            else MainWeatherInfoView()
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

@Composable
private fun MainWeatherInfoView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "New York",
            color = White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "scattered clouds",
            color = TransparentWhite,
            fontSize = 24.sp,
        )
        Spacer(modifier = Modifier.height(14.dp))
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "Weather Icon",
            tint = TransparentWhite,
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "31",
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
                    text = "31",
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
                    text = "30",
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
        modifier = Modifier.fillMaxSize(),
        color = Color.Gray
    ) {
        MainScreen(pageCount = 1) {
            HomeScreen()
        }
    }
}