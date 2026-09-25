package com.barryburgle.gameapp.ui.utilities.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// TODO: use this everywhere HorizontalPager is used
@Composable
fun <T> DottedHorizontalPager(
    items: List<T>,
    modifier: Modifier = Modifier,
    pageSpacing: Dp = 4.dp,
    dotsSpacing: Dp = 6.dp,
    dotsTopPadding: Dp = 8.dp,
    pagerState: PagerState = rememberPagerState(pageCount = { items.size }),
    pageContent: @Composable (item: T, page: Int) -> Unit
) {
    if (items.isEmpty()) return

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = pageSpacing
        ) { page ->
            pageContent(items[page], page)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dotsTopPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dotsSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(items.size) { dotIndex ->
                    val isSelected = pagerState.currentPage == dotIndex
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 7.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
                            )
                    )
                }
            }
        }
    }
}