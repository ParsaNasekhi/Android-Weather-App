package com.parsanasekhi.androidweatherapp.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.parsanasekhi.androidweatherapp.ui.theme.Orange
import com.parsanasekhi.androidweatherapp.ui.theme.TransparentOrange

@Composable
fun LoadingCircleView(
    display: Boolean,
    modifier: Modifier = Modifier
) {
    if (display)
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Orange,
                trackColor = TransparentOrange
            )
        }
}

@Preview(showBackground = true)
@Composable
fun LoadingCircleViewPreview() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingCircleView(true)
        }
    }
}