package com.faridev.gameradar.presentation.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun ImageCarousel(
    modifier: Modifier = Modifier,
    imageResIds: List<Int>,
    isInfinite: Boolean = false,
    scaleAnimationEnabled: Boolean = true,
    showIndicators: Boolean = false,
    autoScrollEnabled: Boolean = true,
    autoScrollDelayMillis: Long = 3000L
) {
    if (imageResIds.isEmpty()) return

    val pageCount = imageResIds.size
    val startPage = if (isInfinite) Int.MAX_VALUE / 2 else 0

    val pagerState = rememberPagerState(
        initialPage = startPage,
        pageCount = { if (isInfinite) Int.MAX_VALUE else pageCount }
    )

    AutoScrollHandler(
        enabled = autoScrollEnabled && pageCount > 1,
        pagerState = pagerState,
        delayMillis = autoScrollDelayMillis,
        isInfinite = isInfinite,
        lastIndex = imageResIds.lastIndex
    )

    Column(modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 10.dp,
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) { page ->
            val actualIndex = page % pageCount
            ImageCard(
                imageRes = imageResIds[actualIndex],
                scaleAnimationEnabled = scaleAnimationEnabled,
                pageOffset = (pagerState.currentPage - page +
                        pagerState.currentPageOffsetFraction).absoluteValue
            )
        }

        if (showIndicators && pageCount > 1) {
            Spacer(modifier = Modifier.height(16.dp))
            PageIndicators(
                pageCount = pageCount,
                currentPage = pagerState.currentPage % pageCount,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun AutoScrollHandler(
    enabled: Boolean,
    pagerState: PagerState,
    delayMillis: Long,
    isInfinite: Boolean,
    lastIndex: Int
) {
    LaunchedEffect(enabled) {
        if (!enabled) return@LaunchedEffect
        while (true) {
            delay(delayMillis)
            val nextPage = if (isInfinite || pagerState.currentPage < lastIndex)
                pagerState.currentPage + 1 else 0
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(600)
            )
        }
    }
}

@Composable
private fun ImageCard(
    imageRes: Int,
    scaleAnimationEnabled: Boolean,
    pageOffset: Float
) {
    val cardModifier = if (scaleAnimationEnabled) {
        Modifier.graphicsLayer {
            scaleY = lerp(0.85f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
        }
    } else Modifier

    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Image(
            modifier = Modifier
                .aspectRatio(2.5f)
                .fillMaxWidth(),
            painter = painterResource(imageRes),
            contentScale = ContentScale.Crop,
            contentDescription = "Banner"
        )
    }
}

@Composable
private fun PageIndicators(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(pageCount) { index ->
            val isCurrentPage = index == currentPage

            // Animated width based on selection
            val animatedWidth by animateDpAsState(
                targetValue = if (isCurrentPage) 20.dp else 8.dp,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                ),
                label = "indicator_width"
            )

            // Animated color based on selection
            val animatedColor by animateColorAsState(
                targetValue = if (isCurrentPage)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                ),
                label = "indicator_color"
            )

            Box(
                modifier = Modifier
                    .size(
                        width = animatedWidth,
                        height = 8.dp
                    )
                    .background(
                        color = animatedColor,
                        shape = CircleShape
                    )
            )
        }
    }
}