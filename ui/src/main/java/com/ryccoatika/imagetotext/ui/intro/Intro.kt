package com.ryccoatika.imagetotext.ui.intro

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.intro.views.DotIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Intro(
    openHomeScreen: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val introDrawables = listOf(R.drawable.intro_1, R.drawable.intro_2, R.drawable.intro_3)
    val introCaptions =
        listOf(R.string.intro_caption_1, R.string.intro_caption_2, R.string.intro_caption_3)

    val pagerState = rememberPagerState {
        introDrawables.size
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                Column(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.extraLarge)
                        .fillMaxSize(),
                ) {
                    Image(
                        painter = painterResource(id = introDrawables[page]),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    )
                    Spacer(Modifier.height(MaterialTheme.spacing.small))
                    Text(
                        stringResource(introCaptions[page]),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary),
            ) {
                TextButton(onClick = openHomeScreen) {
                    Text(
                        stringResource(R.string.button_skip).uppercase(),
                        color = MaterialTheme.colors.onPrimary,
                    )
                }
                DotIndicator(
                    count = pagerState.pageCount,
                    activeIndex = pagerState.currentPage,
                    modifier = Modifier.weight(1f),
                )
                if (pagerState.currentPage == pagerState.pageCount - 1) {
                    TextButton(onClick = openHomeScreen) {
                        Text(
                            stringResource(R.string.button_finish).uppercase(),
                            color = MaterialTheme.colors.onPrimary,
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
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
            openHomeScreen = {},
        )
    }
}
