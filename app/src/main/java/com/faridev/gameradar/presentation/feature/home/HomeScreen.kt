package com.faridev.gameradar.presentation.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToDetail: (gameId: Int) -> Unit
) {
    Text("Home Screen")
}