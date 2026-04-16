package com.faridev.gameradar.presentation.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.faridev.gameradar.R
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.presentation.common.components.ErrorItem
import com.faridev.gameradar.presentation.common.components.FullWidthLoader
import com.faridev.gameradar.presentation.common.components.GamesItemCard
import com.faridev.gameradar.presentation.common.components.ImageCarousel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToDetail: (gameId: Int) -> Unit,
) {
    val gamesLazyPagingItems = viewModel.gamesFlow.collectAsLazyPagingItems()

    Box(Modifier.fillMaxSize()) {
        GamesListScreen(
            items = gamesLazyPagingItems,
            onNavigateToDetail = onNavigateToDetail,
        )
    }
}

@Composable
private fun GamesListScreen(
    items: LazyPagingItems<GameResult>,
    onNavigateToDetail: (Int) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(5.dp),
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
            count = items.itemCount,
            key = { index -> items[index]?.id ?: index },
        ) { index ->
            val game = items[index]
            game?.let {
                GamesItemCard(
                    modifier = Modifier.padding(5.dp),
                    item = it,
                    onNavigateToDetail = onNavigateToDetail,
                )
            }
        }

        when {
            // Initial loading
            items.loadState.refresh is LoadState.Loading -> item(span = { GridItemSpan(2) }) { FullWidthLoader() }
            // Loading more items
            items.loadState.append is LoadState.Loading -> item(span = { GridItemSpan(2) }) { FullWidthLoader(small = true) }

            // Error states
            items.loadState.refresh is LoadState.Error -> {
                val error = items.loadState.refresh as LoadState.Error
                item(span = { GridItemSpan(2) }) {
                    ErrorItem(
                        message = error.error.localizedMessage ?: "Unknown error occurred",
                        onRetry = { items.retry() },
                    )
                }
            }

            items.loadState.append is LoadState.Error -> {
                val error = items.loadState.append as LoadState.Error
                item(span = { GridItemSpan(2) }) {
                    ErrorItem(
                        message = error.error.localizedMessage ?: "Error loading more items",
                        onRetry = { items.retry() },
                    )
                }
            }
        }
    }
}
