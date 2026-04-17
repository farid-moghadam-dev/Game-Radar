package com.faridev.gameradar.presentation.common.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.bitmapConfig
import coil3.request.crossfade
import com.faridev.gameradar.R

@Composable
fun ImageWithOverlay(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    overlayColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Box(modifier = modifier) {
        AsyncImage(
            modifier = Modifier.matchParentSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            placeholder = painterResource(R.drawable.gaming_banner_placeholder),
            error = painterResource(R.drawable.gaming_banner_placeholder),
            contentDescription = "Image With Overlay",
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(overlayColor),
        )
    }
}
