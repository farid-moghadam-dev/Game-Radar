package com.faridev.gameradar.presentation.common.components

import android.graphics.Bitmap
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.bitmapConfig
import coil3.request.crossfade
import coil3.request.placeholder
import com.faridev.gameradar.R
import com.faridev.gameradar.domain.model.GameResult

@Composable
fun GamesItemCard(
    modifier: Modifier = Modifier,
    item: GameResult,
    onNavigateToDetail: (gameId: Int) -> Unit,
) {
    // Create stable lambda to avoid recomposition
    val onItemClick = remember(item.id) {
        { onNavigateToDetail(item.id) }
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        AnimatedTouchBox(
            modifier = Modifier.fillMaxWidth(),
            overlayColor = MaterialTheme.colorScheme.onSurface,
            onClick = onItemClick,
            backgroundContent = {
                GameBackgroundImage(
                    imageUrl = item.backgroundImage,
                )
            },
            foregroundContent = { isPressed ->
                GameForegroundContent(
                    isPressed = isPressed,
                    gameName = item.name,
                )
            },
        )
    }
}

@Composable
private fun GameBackgroundImage(
    imageUrl: String?,
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
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun BoxScope.GameForegroundContent(
    isPressed: State<Boolean>,
    gameName: String,
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
            textAlign = TextAlign.Center,
        )
    }
}
