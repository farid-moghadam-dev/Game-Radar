@file:OptIn(ExperimentalMaterial3Api::class)

package com.faridev.gameradar.presentation.feature.detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.faridev.gameradar.R
import com.faridev.gameradar.core.util.checkUrlValidation
import com.faridev.gameradar.core.util.iconResId
import com.faridev.gameradar.core.util.loadJSONFromAsset
import com.faridev.gameradar.core.util.noRippleClickable
import com.faridev.gameradar.core.util.showShortToast
import com.faridev.gameradar.data.mapper.toDomain
import com.faridev.gameradar.data.model.GameDetailsResDto
import com.faridev.gameradar.domain.model.GameDetails
import com.faridev.gameradar.domain.model.ParentPlatformInfo
import com.faridev.gameradar.domain.model.Store
import com.faridev.gameradar.presentation.common.components.AnimatedTouchBox
import com.faridev.gameradar.presentation.common.components.ClickableWordsText
import com.faridev.gameradar.presentation.common.components.ErrorItem
import com.faridev.gameradar.presentation.common.components.ExpandableText
import com.faridev.gameradar.presentation.common.components.ShowDefaultTextTooltip
import com.faridev.gameradar.presentation.common.theme.Fonts
import com.faridev.gameradar.presentation.common.theme.HighRateColor
import com.faridev.gameradar.presentation.common.theme.LightHyperlinkColor
import com.faridev.gameradar.presentation.common.theme.LowRateColor
import com.faridev.gameradar.presentation.common.theme.MediumRateColor
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(gameId: Int) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val gameDetails = remember { prepareGameDetailMockData(context) }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (gameDetails == null) {
            ErrorItem(
                message = "Game Detail Mock Data Error",
                onRetry = { /* fetch game detail again */ }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                DetailTopBar(gameDetails = gameDetails)

                DetailContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    gameDetails = gameDetails
                )
            }
        }
    }
}

@Composable
private fun DetailTopBar(
    modifier: Modifier = Modifier,
    gameDetails: GameDetails
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        ImageWithOverlay(gameDetails.backgroundImageAdditional ?: gameDetails.backgroundImage ?: "")

        Row(
            Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TopBarDetail(
                modifier = Modifier.weight(0.6f),
                gameDetails = gameDetails
            )

            Card(
                Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = gameDetails.backgroundImage,
                    contentDescription = "Banner Image",
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
private fun ImageWithOverlay(imageUrl: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imageUrl,
            contentDescription = "Image With Overlay",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.gaming_banner_placeholder),
            error = painterResource(R.drawable.gaming_banner_placeholder)
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.onSurface)
        )
    }
}

@Composable
private fun TopBarDetail(modifier: Modifier = Modifier, gameDetails: GameDetails) {
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(4.dp)
            ) {
                gameDetails.released?.let {
                    Text(
                        modifier = Modifier.padding(3.dp),
                        text = gameDetails.released,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = Fonts.ArialRounded
                    )
                }
            }

            gameDetails.name?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = gameDetails.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(Modifier.height(15.dp))

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Average Playtime: ${gameDetails.playtime ?: "N/A"}",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontFamily = Fonts.ArialRounded
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Card(
                    modifier = Modifier.noRippleClickable {
                        gameDetails.metacriticUrl?.checkUrlValidation(
                            onValidUrl = { validUrl ->
                                openUrlInBrowser(context = context, url = validUrl)
                            },
                            onUrlValidationError = {
                                context.showShortToast("Metacritic domain is not valid")
                            }
                        )
                    },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_meta),
                            contentDescription = "Metacritic Rate",
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = "${gameDetails.metacritic ?: "N/A"}",
                            color = Color.Black,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 14.sp
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = getColorFromRate(
                            gameDetails.rating ?: 0.0, gameDetails.ratingTop?.toDouble() ?: 0.0
                        )
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Max),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 3.dp),
                            painter = painterResource(R.drawable.ic_rate),
                            contentDescription = "Rate icon",
                            tint = Color.White
                        )

                        VerticalDivider(
                            Modifier
                                .fillMaxHeight()
                                .padding(vertical = 3.dp),
                            thickness = (1.5).dp,
                            color = Color.White
                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = "${gameDetails.rating ?: "N/A"}/${gameDetails.ratingTop ?: "N/A"}",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            gameDetails.website?.let { website ->
                Text(
                    modifier = Modifier.noRippleClickable {
                        website.checkUrlValidation(
                            onValidUrl = { validUrl ->
                                openUrlInBrowser(context = context, url = validUrl)
                            },
                            onUrlValidationError = {
                                context.showShortToast("Store domain is not valid")
                            }
                        )
                        openUrlInBrowser(context = context, gameDetails.website)
                    },
                    text = gameDetails.website,
                    color = LightHyperlinkColor,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = TextDecoration.Underline
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentPadding = PaddingValues(horizontal = 3.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            items(gameDetails.parentPlatforms.mapNotNull { it.platform }) { parentPlatform ->
                PlatformIcon(parentPlatform)
            }
        }
    }
}

@Composable
private fun DetailContent(
    modifier: Modifier = Modifier,
    gameDetails: GameDetails
) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        gameDetails.descriptionRaw?.let {
            DetailsData("About") {
                ExpandableText(
                    modifier = Modifier.fillMaxWidth(),
                    text = gameDetails.descriptionRaw,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 5,
                    lineHeight = 18.sp
                )
            }
        }

        Row(Modifier.fillMaxWidth()) {
            gameDetails.released?.let {
                DetailsData(modifier = Modifier.weight(1f), title = "Release Date") {
                    Text(
                        text = gameDetails.released,
                        style = MaterialTheme.typography.labelLarge,
                        lineHeight = 18.sp
                    )
                }
            }

            DetailsData(modifier = Modifier.weight(1f), title = "Age Rating") {
                ShowDefaultTextTooltip(
                    tooltipText = gameDetails.esrbRating.description
                ) { tooltipState, scope ->
                    Row(
                        modifier = Modifier.clickable {
                            scope.launch { tooltipState.show() }
                        },
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .height(25.dp)
                                .aspectRatio(0.75f),
                            painter = painterResource(gameDetails.esrbRating.iconResId),
                            contentScale = ContentScale.Fit,
                            contentDescription = "ESRB Rating"
                        )

                        Text(
                            text = gameDetails.esrbRating.title,
                            style = MaterialTheme.typography.labelLarge,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        if (gameDetails.genres.isNotEmpty()) {
            DetailsData("Genres") {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = gameDetails.genres.mapNotNull { it.name }.joinToString(", "),
                    style = MaterialTheme.typography.labelLarge,
                    lineHeight = 18.sp
                )
            }
        }

        if (gameDetails.publishers.isNotEmpty()) {
            DetailsData("Publisher") {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = gameDetails.publishers.mapNotNull { it.name }.joinToString(", "),
                    style = MaterialTheme.typography.labelLarge,
                    lineHeight = 18.sp
                )
            }
        }

        if (gameDetails.developers.isNotEmpty()) {
            DetailsData("Developer") {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = gameDetails.developers.mapNotNull { it.name }.joinToString(", "),
                    style = MaterialTheme.typography.labelLarge,
                    lineHeight = 18.sp
                )
            }
        }

        if (gameDetails.platforms.isNotEmpty()) {
            DetailsData("Platforms") {
                ClickableWordsText(
                    text = gameDetails.platforms.mapNotNull { it.platform?.name }
                        .joinToString(", "),
                    style = MaterialTheme.typography.labelLarge,
                    lineHeight = 18.sp
                ) { clickedWord ->
                    //TODO show clicked platform games in future
                    context.showShortToast("Platform : $clickedWord clicked")
                }
            }
        }

        if (gameDetails.stores.isNotEmpty()) {
            DetailsData("Stores") {
                LazyRow {
                    items(gameDetails.stores.mapNotNull { it.store }) { store ->
                        StoreItem(store)
                    }
                }
            }
        }

        if (gameDetails.tags.isNotEmpty()) {
            DetailsData("Tags") {
                ClickableWordsText(
                    text = gameDetails.tags.mapNotNull { it.name }.joinToString(", "),
                    style = MaterialTheme.typography.labelLarge,
                    lineHeight = 18.sp,
                ) { clickedWord ->
                    //TODO show clicked games with clicked tag in future
                    context.showShortToast("Tag : $clickedWord clicked")
                }
            }
        }
    }
}

@Composable
private fun DetailsData(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.padding(vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        content()
    }
}

@Composable
private fun PlatformIcon(platform: ParentPlatformInfo) {
    val icon = painterResource(id = platform.iconResId())
    Image(
        modifier = Modifier
            .padding(horizontal = 5.dp),
        painter = icon,
        contentScale = ContentScale.Inside,
        contentDescription = platform.name
    )
}

@Composable
private fun StoreItem(store: Store) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .height(120.dp)
            .aspectRatio(1.7f)
            .padding(horizontal = 5.dp)
    ) {
        AnimatedTouchBox(
            overlayColor = MaterialTheme.colorScheme.onSurface,
            onClick = {
                store.domain?.checkUrlValidation(
                    onValidUrl = { validUrl ->
                        openUrlInBrowser(context = context, url = validUrl)
                    },
                    onUrlValidationError = {
                        context.showShortToast("Store domain is not valid")
                    }
                )
            },
            backgroundContent = {
                ImageWithOverlay(imageUrl = store.imageBackground ?: "")
            },
            foregroundContent = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        3.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    StoreIcon(store)
                    Text(
                        text = store.name ?: "N/A",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            }
        )
    }
}

@Composable
private fun StoreIcon(store: Store) {
    val icon = painterResource(id = store.iconResId())
    Icon(
        modifier = Modifier
            .padding(horizontal = 5.dp),
        contentDescription = store.name,
        painter = icon,
        tint = Color.White
    )
}

fun getColorFromRate(rate: Double, rateTop: Double): Color {
    if (rateTop <= 0) return Color.Gray

    val normalized = rate / rateTop
    return when {
        normalized <= 0.4 -> LowRateColor
        normalized < 0.7 -> MediumRateColor
        else -> HighRateColor
    }
}

private fun openUrlInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}

private fun prepareGameDetailMockData(context: Context): GameDetails? {
    val json = context.loadJSONFromAsset("game-detail-fake-data.json").orEmpty()

    val jsonBuilder = Json {
        isLenient = true
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    return try {
        jsonBuilder.decodeFromString<GameDetailsResDto>(json).toDomain()
    } catch (ex: SerializationException) {
        Timber.e("Error in prepareGameDetailMockData -> ${ex.message}")
        null
    }
}