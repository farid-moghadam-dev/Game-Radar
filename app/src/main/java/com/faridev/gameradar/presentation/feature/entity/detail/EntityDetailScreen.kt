package com.faridev.gameradar.presentation.feature.entity.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.faridev.gameradar.core.util.stripHtml
import com.faridev.gameradar.domain.model.GameEntityDetails
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.presentation.common.components.DetailSection
import com.faridev.gameradar.presentation.common.components.ErrorItem
import com.faridev.gameradar.presentation.common.components.ExpandableText
import com.faridev.gameradar.presentation.common.components.FullWidthLoader
import com.faridev.gameradar.presentation.common.components.GamesItemCard
import com.faridev.gameradar.presentation.common.components.ImageWithOverlay
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
        columns = GridCells.Fixed(2)
    ) {
        item(span = { GridItemSpan(2) }) {
            EntityHeader(details = details)
        }

        details.description?.stripHtml()?.takeIf { it.isNotBlank() }?.let { text ->
            item(span = { GridItemSpan(2) }) {
                DetailSection(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    title = "About"
                ) {
                    ExpandableText(
                        modifier = Modifier.fillMaxWidth(),
                        text = text,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Justify,
                        maxLines = 5,
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
            DetailSection(
                modifier = Modifier.padding(horizontal = 8.dp),
                title = "Games"
            )
        }

        items(
            count = games.itemCount,
            key = { index -> games[index]?.id ?: index }
        ) { index ->
            games[index]?.let { game ->
                GamesItemCard(
                    modifier = Modifier.padding(5.dp),
                    item = game,
                    onNavigateToDetail = onNavigateToGame
                )
            }
        }

        when {
            games.loadState.refresh is LoadState.Loading ->
                item(span = { GridItemSpan(2) }) { FullWidthLoader() }
            games.loadState.append is LoadState.Loading ->
                item(span = { GridItemSpan(2) }) { FullWidthLoader(small = true) }
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
            .height(200.dp)
    ) {
        ImageWithOverlay(
            modifier = Modifier.fillMaxSize(),
            details.imageBackground
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

