package com.faridev.gameradar.presentation.feature.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faridev.gameradar.R
import com.faridev.gameradar.presentation.common.components.ImageCarousel

@Composable
fun HomeScreen(
    onNavigateToDetail: (gameId: Int) -> Unit
) {
    ImageCarousel(
        modifier = Modifier
            .fillMaxWidth(),
        imageResIds = listOf(
            R.drawable.main_banner_1,
            R.drawable.main_banner_3,
            R.drawable.main_banner_2,
        ),
        isInfinite = true,
        showIndicators = true
    )
}