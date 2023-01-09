package com.ryccoatika.imagetotext.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.ui.DotIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun Intro() {
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
                    Spacer(Modifier.height(10.dp))
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
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                TextButton(onClick = { }) {
                    Text(
                        stringResource(R.string.button_skip).uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                DotIndicator(
                    count = pagerState.pageCount,
                    activeIndex = pagerState.currentPage,
                    modifier = Modifier.weight(1f)
                )
                if (pagerState.currentPage == pagerState.pageCount - 1) {
                    TextButton(onClick = { }) {
                        Text(
                            stringResource(R.string.button_finish).uppercase(),
                            color = MaterialTheme.colorScheme.onPrimary
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
                            tint = MaterialTheme.colorScheme.onPrimary,
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
    Intro()
}