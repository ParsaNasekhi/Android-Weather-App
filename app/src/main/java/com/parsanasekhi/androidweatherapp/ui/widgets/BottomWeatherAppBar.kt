package com.parsanasekhi.androidweatherapp.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        containerColor = Transparent
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
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home Icon",
                    modifier = Modifier.size(28.dp),
                    tint = if (page.intValue == 0) White else TransparentWhite
                )
            }
            IconButton(onClick = {
                page.intValue = 1
                onBookmarkClicked()
            }) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    modifier = Modifier.size(28.dp),
                    contentDescription = "Bookmark Icon",
                    tint = if (page.intValue == 0) TransparentWhite else White
                )
            }
        }
    }
}
