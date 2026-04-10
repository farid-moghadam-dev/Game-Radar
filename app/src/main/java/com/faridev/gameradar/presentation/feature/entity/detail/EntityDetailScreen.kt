package com.faridev.gameradar.presentation.feature.entity.detail

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.bitmapConfig
import coil3.request.crossfade
import com.faridev.gameradar.R
import com.faridev.gameradar.core.util.stripHtml
import com.faridev.gameradar.domain.model.GameEntityDetails
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.presentation.common.components.ErrorItem
import com.faridev.gameradar.presentation.common.components.ExpandableText
import com.faridev.gameradar.presentation.common.state.UiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun EntityDetailScreen(
    type: GameEntityType,
    entityId: Int,
    viewModel: EntityDetailViewModel = koinViewModel(),
    onNavigateToGame: (gameId: Int) -> Unit
) {
    LaunchedEffect(type, entityId) { viewModel.load(type, entityId) }

    val games = viewModel.gamesFlow.collectAsLazyPagingItems()

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val state = viewModel.detailsState) {
            is UiState.Error -> ErrorItem(message = state.message, onRetry = { viewModel.retry() })
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> EntityDetailContent(
                details = state.data,
                games = games,
                onNavigateToGame = onNavigateToGame
            )
        }
    }
}

@Composable
private fun EntityDetailContent(
    details: GameEntityDetails,
    games: LazyPagingItems<GameResult>,
    onNavigateToGame: (Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(5.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            EntityHeader(details = details)
        }

        details.description?.stripHtml()?.takeIf { it.isNotBlank() }?.let { text ->
            item(span = { GridItemSpan(2) }) {
                Column(Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(6.dp))
                    ExpandableText(
                        modifier = Modifier.fillMaxWidth(),
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 6,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        if (details.domain != null) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    text = "Domain: ${details.domain}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item(span = { GridItemSpan(2) }) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
                text = "Games",
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(
            count = games.itemCount,
            key = { index -> games[index]?.id ?: index }
        ) { index ->
            games[index]?.let { game ->
                GameCard(game = game, onClick = { onNavigateToGame(game.id) })
            }
        }

        when {
            games.loadState.refresh is LoadState.Loading ->
                item(span = { GridItemSpan(2) }) { CenteredLoader() }
            games.loadState.append is LoadState.Loading ->
                item(span = { GridItemSpan(2) }) { CenteredLoader(small = true) }
            games.loadState.refresh is LoadState.Error -> {
                val e = games.loadState.refresh as LoadState.Error
                item(span = { GridItemSpan(2) }) {
                    ErrorItem(
                        message = e.error.localizedMessage ?: "Unknown error",
                        onRetry = { games.retry() }
                    )
                }
            }
            games.loadState.append is LoadState.Error -> {
                val e = games.loadState.append as LoadState.Error
                item(span = { GridItemSpan(2) }) {
                    ErrorItem(
                        message = e.error.localizedMessage ?: "Error loading more",
                        onRetry = { games.retry() }
                    )
                }
            }
        }
    }
}

@Composable
private fun EntityHeader(details: GameEntityDetails) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(details.imageBackground)
                .crossfade(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            placeholder = painterResource(R.drawable.gaming_banner_placeholder),
            error = painterResource(R.drawable.gaming_banner_placeholder),
            contentDescription = details.name,
            contentScale = ContentScale.Crop
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = details.name,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${details.gamesCount} games",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun GameCard(
    game: GameResult,
    onClick: () -> Unit
) {
    val click = remember(game.id) { onClick }
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(160.dp)
            .clickable(onClick = click),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(game.backgroundImage)
                    .crossfade(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build(),
                placeholder = painterResource(R.drawable.ic_placeholder),
                error = painterResource(R.drawable.ic_placeholder),
                contentDescription = game.name,
                contentScale = ContentScale.Crop
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                text = game.name,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CenteredLoader(small: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = if (small) Modifier.size(24.dp) else Modifier
        )
    }
}
