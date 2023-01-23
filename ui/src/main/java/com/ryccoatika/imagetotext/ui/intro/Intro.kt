package com.ryccoatika.imagetotext.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.ui.DotIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Intro(
    openHomeScreen: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val introDrawables = listOf(R.drawable.intro_1, R.drawable.intro_2, R.drawable.intro_3)
    val introCaptions =
        listOf(R.string.intro_caption_1, R.string.intro_caption_2, R.string.intro_caption_3)

    val pagerState = rememberPagerState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            HorizontalPager(
                count = introDrawables.size,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                Column(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.extraLarge)
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = introDrawables[page]),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    Spacer(Modifier.height(MaterialTheme.spacing.small))
                    Text(
                        stringResource(introCaptions[page]),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
            ) {
                TextButton(onClick = openHomeScreen) {
                    Text(
                        stringResource(R.string.button_skip).uppercase(),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                DotIndicator(
                    count = pagerState.pageCount,
                    activeIndex = pagerState.currentPage,
                    modifier = Modifier.weight(1f)
                )
                if (pagerState.currentPage == pagerState.pageCount - 1) {
                    TextButton(onClick = openHomeScreen) {
                        Text(
                            stringResource(R.string.button_finish).uppercase(),
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage+1)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onPrimary,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun IntroPreview() {
    AppTheme {
        Intro(
            openHomeScreen = {}
        )
    }
}