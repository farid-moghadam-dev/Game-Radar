package com.faridev.gameradar.presentation.feature.home

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.bitmapConfig
import coil3.request.crossfade
import coil3.request.placeholder
import com.faridev.gameradar.R
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.presentation.common.components.AnimatedTouchBox
import com.faridev.gameradar.presentation.common.components.ImageCarousel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToDetail: (gameId: Int) -> Unit
) {
    val gamesLazyPagingItems = viewModel.gamesFlow.collectAsLazyPagingItems()

    Box(Modifier.fillMaxSize()) {
        GamesListScreen(
            gamesLazyPagingItems = gamesLazyPagingItems,
            onNavigateToDetail = onNavigateToDetail
        )
    }
}

@Composable
private fun GamesListScreen(
    gamesLazyPagingItems: LazyPagingItems<GameResult>,
    onNavigateToDetail: (Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(5.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            ImageCarousel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                imageResIds = listOf(
                    R.drawable.main_banner_1,
                    R.drawable.main_banner_3,
                    R.drawable.main_banner_2,
                ),
                isInfinite = true,
            )
        }
        items(
            count = gamesLazyPagingItems.itemCount,
            key = { index -> gamesLazyPagingItems[index]?.id ?: index }
        ) { index ->
            val game = gamesLazyPagingItems[index]
            game?.let {
                GamesItemCard(
                    item = it,
                    onNavigateToDetail = onNavigateToDetail
                )
            }
        }

        // Handle loading states
        gamesLazyPagingItems.apply {
            when {
                // Initial loading
                loadState.refresh is LoadState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                // Loading more items
                loadState.append is LoadState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                }

                // Error states
                loadState.refresh is LoadState.Error -> {
                    val error = gamesLazyPagingItems.loadState.refresh as LoadState.Error
                    item(span = { GridItemSpan(2) }) {
                        ErrorItem(
                            message = error.error.localizedMessage ?: "Unknown error occurred",
                            onRetry = { gamesLazyPagingItems.retry() }
                        )
                    }
                }

                loadState.append is LoadState.Error -> {
                    val error = gamesLazyPagingItems.loadState.append as LoadState.Error
                    item(span = { GridItemSpan(2) }) {
                        ErrorItem(
                            message = error.error.localizedMessage ?: "Error loading more items",
                            onRetry = { gamesLazyPagingItems.retry() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorItem(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text("Retry", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}

@Composable
private fun GamesItemCard(
    item: GameResult,
    onNavigateToDetail: (gameId: Int) -> Unit
) {
    // Create stable lambda to avoid recomposition
    val onItemClick = remember(item.id) {
        { onNavigateToDetail(item.id) }
    }

    Card(
        modifier = Modifier.padding(5.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        AnimatedTouchBox(
            modifier = Modifier.fillMaxWidth(),
            overlayColor = MaterialTheme.colorScheme.onSurface,
            onClick = onItemClick,
            backgroundContent = {
                GameBackgroundImage(
                    imageUrl = item.backgroundImage
                )
            },
            foregroundContent = { isPressed ->
                GameForegroundContent(
                    isPressed = isPressed,
                    gameName = item.name
                )
            }
        )
    }
}

@Composable
private fun GameBackgroundImage(
    imageUrl: String?
) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .placeholder(R.drawable.ic_placeholder)
            .build(),
        placeholder = painterResource(R.drawable.ic_placeholder),
        contentDescription = "Game Image",
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun BoxScope.GameForegroundContent(
    isPressed: State<Boolean>,
    gameName: String
) {
    if (isPressed.value) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(5.dp),
            text = gameName,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}