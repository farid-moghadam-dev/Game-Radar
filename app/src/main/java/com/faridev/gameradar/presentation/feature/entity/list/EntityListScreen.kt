package com.faridev.gameradar.presentation.feature.entity.list

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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.faridev.gameradar.domain.model.GameEntity
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.presentation.common.components.AnimatedTouchBox
import com.faridev.gameradar.presentation.common.components.ErrorItem
import com.faridev.gameradar.presentation.common.components.ImageWithOverlay
import org.koin.androidx.compose.koinViewModel

@Composable
fun EntityListScreen(
    type: GameEntityType,
    viewModel: EntityListViewModel = koinViewModel(),
    onNavigateToDetail: (type: GameEntityType, entityId: Int) -> Unit
) {
    LaunchedEffect(type) { viewModel.setType(type) }

    val items = viewModel.entitiesFlow.collectAsLazyPagingItems()

    Box(Modifier.fillMaxSize()) {
        EntityList(items = items, type = type, onNavigateToDetail = onNavigateToDetail)
    }
}

@Composable
private fun EntityList(
    items: LazyPagingItems<GameEntity>,
    type: GameEntityType,
    onNavigateToDetail: (GameEntityType, Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = items.itemCount,
            key = { index -> items[index]?.id ?: index }
        ) { index ->
            items[index]?.let { entity ->
                EntityCard(
                    entity = entity,
                    onClick = { onNavigateToDetail(type, entity.id) }
                )
            }
        }

        when {
            items.loadState.refresh is LoadState.Loading -> item { FullWidthLoader() }
            items.loadState.append is LoadState.Loading -> item { FullWidthLoader(small = true) }
            items.loadState.refresh is LoadState.Error -> {
                val error = items.loadState.refresh as LoadState.Error
                item {
                    ErrorItem(
                        message = error.error.localizedMessage ?: "Unknown error",
                        onRetry = { items.retry() }
                    )
                }
            }
            items.loadState.append is LoadState.Error -> {
                val error = items.loadState.append as LoadState.Error
                item {
                    ErrorItem(
                        message = error.error.localizedMessage ?: "Error loading more",
                        onRetry = { items.retry() }
                    )
                }
            }
        }
    }
}

@Composable
private fun FullWidthLoader(small: Boolean = false) {
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

@Composable
private fun EntityCard(
    entity: GameEntity,
    onClick: () -> Unit
) {
    val click = remember(entity.id) { onClick }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(Modifier.fillMaxSize()) {
            AnimatedTouchBox(
                modifier = Modifier.fillMaxSize(),
                overlayColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                onClick = click,
                backgroundContent = {
                    ImageWithOverlay(
                        modifier = Modifier.fillMaxSize(),
                        imageUrl = entity.imageBackground,
                        overlayColor = MaterialTheme.colorScheme.onSurface.copy(0.4f)
                    )
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = entity.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${entity.gamesCount} games",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.labelLarge
                )
                if (entity.topGames.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = entity.topGames.take(3).joinToString(" • ") { it.name },
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}