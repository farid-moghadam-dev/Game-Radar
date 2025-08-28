package com.faridev.gameradar.presentation.feature.home

import android.content.Context
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.bitmapConfig
import coil3.request.crossfade
import coil3.request.placeholder
import com.faridev.gameradar.R
import com.faridev.gameradar.core.util.loadJSONFromAsset
import com.faridev.gameradar.data.mapper.toDomain
import com.faridev.gameradar.data.model.GamesListResDto
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.domain.model.GamesList
import com.faridev.gameradar.presentation.common.components.AnimatedTouchBox
import com.faridev.gameradar.presentation.common.components.ImageCarousel
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber

@Composable
fun HomeScreen(
    onNavigateToDetail: (gameId: Int) -> Unit
) {
    val context = LocalContext.current
    val gamesList = remember { prepareGamesListMockData(context)?.results }

    Box(Modifier.fillMaxSize()) {
        GamesListScreen(
            gamesList = gamesList,
            onNavigateToDetail = onNavigateToDetail
        )
    }
}

@Composable
private fun GamesListScreen(
    gamesList: List<GameResult>?,
    onNavigateToDetail: (Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(5.dp)
    ) {
        // Banner carousel at top
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
                isInfinite = true
            )
        }

        if (!gamesList.isNullOrEmpty()) {
            items(
                items = gamesList,
                key = { it.id }
            ) { game ->
                GamesItemCard(
                    item = game,
                    onNavigateToDetail = onNavigateToDetail
                )
            }
        } else {
            item(span = { GridItemSpan(2) }) {
                ErrorItem(
                    message = "Games List Mock Data Error",
                    onRetry = { /* fetch games list again */ }
                )
            }
        }
    }
}

@Composable
private fun GamesItemCard(
    item: GameResult,
    onNavigateToDetail: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        // Stable lambda to avoid unnecessary recompositions
        val onItemClick by rememberUpdatedState(newValue = { onNavigateToDetail(item.id) })

        AnimatedTouchBox(
            modifier = Modifier.fillMaxWidth(),
            overlayColor = MaterialTheme.colorScheme.onSurface,
            onClick = onItemClick,
            backgroundContent = {
                GameBackgroundImage(item.backgroundImage)
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
private fun GameBackgroundImage(imageUrl: String?) {
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
        contentDescription = null,
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

// Load and parse mock JSON
private fun prepareGamesListMockData(context: Context): GamesList? {
    val json = context.loadJSONFromAsset("games-list-fake-data.json").orEmpty()
    val jsonBuilder = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
    return try {
        jsonBuilder.decodeFromString<GamesListResDto>(json).toDomain()
    } catch (ex: SerializationException) {
        Timber.e("Error in prepareGamesListMockData -> ${ex.message}")
        null
    }
}