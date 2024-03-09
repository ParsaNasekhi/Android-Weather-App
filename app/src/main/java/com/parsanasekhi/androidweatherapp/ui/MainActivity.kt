package com.parsanasekhi.androidweatherapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.parsanasekhi.androidweatherapp.R
import com.parsanasekhi.androidweatherapp.ui.screens.bookmark.BookmarkScreen
import com.parsanasekhi.androidweatherapp.ui.screens.home.HomeScreen
import com.parsanasekhi.androidweatherapp.ui.theme.AndroidWeatherAppTheme
import com.parsanasekhi.androidweatherapp.ui.theme.Transparent
import com.parsanasekhi.androidweatherapp.ui.widgets.BottomWeatherAppBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidWeatherAppTheme {

                var insets: Insets? = null
                val currentView = LocalView.current
                ViewCompat.setOnApplyWindowInsetsListener(currentView) { view, windowInsets ->
                    insets =
                        windowInsets.getInsets(WindowInsetsCompat.Type.mandatorySystemGestures())
                    view.updatePadding(insets!!.left, insets!!.top, insets!!.right, insets!!.bottom)
                    WindowInsetsCompat.CONSUMED
                }

                val pagerState = rememberPagerState { 2 }

                Scaffold(
                    contentWindowInsets = WindowInsets(
                        insets?.left ?: 0,
                        insets?.top ?: 0,
                        insets?.right ?: 0,
                        insets?.bottom ?: 0
                    )
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainScreen(pagerState) { page ->
                            if (page == 0) HomeScreen()
                            else BookmarkScreen(pagerState)
                        }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    pagerState: PagerState,
    pagerContent: @Composable (Int) -> Unit
) {

    val scope = rememberCoroutineScope()
    val page = remember {
        mutableIntStateOf(0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.app_background),
            contentDescription = "App Background",
            contentScale = ContentScale.Crop
        )
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            bottomBar = {
                Column {
                    BottomWeatherAppBar(
                        page = page,
                        onHomeClicked = {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = 0,
                                    animationSpec = tween(durationMillis = 1000)
                                )
                            }
                        },
                        onBookmarkClicked = {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = 1,
                                    animationSpec = tween(durationMillis = 1000)
                                )
                            }
                        }
                    )
                }
            },
            containerColor = Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxHeight()
                ) { newPage ->
                    if (newPage == pagerState.currentPage)
                        page.intValue = newPage
                    pagerContent(newPage)
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                }

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
//        MainScreen()
    }
}