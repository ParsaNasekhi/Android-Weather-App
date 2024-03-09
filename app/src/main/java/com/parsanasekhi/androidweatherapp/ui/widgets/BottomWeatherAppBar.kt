package com.parsanasekhi.androidweatherapp.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.parsanasekhi.androidweatherapp.R
import com.parsanasekhi.androidweatherapp.ui.theme.Transparent
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentWhite
import com.parsanasekhi.androidweatherapp.ui.theme.White

@Composable
fun BottomWeatherAppBar(
    modifier: Modifier = Modifier,
    page: MutableIntState,

    onHomeClicked: () -> Unit,
    onBookmarkClicked: () -> Unit,
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = Transparent,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {
                page.intValue = 0
                onHomeClicked()
            }) {
                Image(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home Icon",
                    modifier = Modifier
                        .height(30.dp)
                        .wrapContentWidth(),
                    colorFilter = if (page.intValue == 0) ColorFilter.tint(White) else ColorFilter.tint(
                        TransparentWhite
                    ),
                    contentScale = ContentScale.FillHeight
                )
            }
            IconButton(onClick = {
                page.intValue = 1
                onBookmarkClicked()
            }) {
                Image(
                    painter = painterResource(id = R.drawable.bookmark),
                    modifier = Modifier
                        .height(28.dp)
                        .wrapContentWidth(),
                    contentDescription = "Bookmark Icon",
                    colorFilter = if (page.intValue == 0) ColorFilter.tint(TransparentWhite) else ColorFilter.tint(
                        White
                    ),
                    contentScale = ContentScale.FillHeight
                )
            }
        }
    }
}
