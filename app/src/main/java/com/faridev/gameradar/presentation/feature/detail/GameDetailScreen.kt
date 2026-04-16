@file:OptIn(ExperimentalMaterial3Api::class)

package com.faridev.gameradar.presentation.feature.detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.faridev.gameradar.R
import com.faridev.gameradar.core.util.checkUrlValidation
import com.faridev.gameradar.core.util.iconResId
import com.faridev.gameradar.core.util.noRippleClickable
import com.faridev.gameradar.core.util.showShortToast
import com.faridev.gameradar.domain.model.GameDetails
import com.faridev.gameradar.domain.model.ParentPlatform
import com.faridev.gameradar.domain.model.ParentPlatformInfo
import com.faridev.gameradar.domain.model.Store
import com.faridev.gameradar.domain.model.StoreInfo
import com.faridev.gameradar.presentation.common.components.AnimatedTouchBox
import com.faridev.gameradar.presentation.common.components.ClickableWordsText
import com.faridev.gameradar.presentation.common.components.DetailSection
import com.faridev.gameradar.presentation.common.components.ErrorItem
import com.faridev.gameradar.presentation.common.components.ExpandableText
import com.faridev.gameradar.presentation.common.components.ImageWithOverlay
import com.faridev.gameradar.presentation.common.components.ShowDefaultTextTooltip
import com.faridev.gameradar.presentation.common.state.UiState
import com.faridev.gameradar.presentation.common.theme.Fonts
import com.faridev.gameradar.presentation.common.theme.HighRateColor
import com.faridev.gameradar.presentation.common.theme.LightHyperlinkColor
import com.faridev.gameradar.presentation.common.theme.LowRateColor
import com.faridev.gameradar.presentation.common.theme.MediumRateColor
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.collections.mapNotNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(viewModel: GameDetailViewModel = koinViewModel(), gameId: Int) {
    val scrollState = rememberScrollState()

    LaunchedEffect(gameId) {
        viewModel.load(gameId)
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val state = viewModel.detailsState) {
            is UiState.Error -> ErrorItem(message = state.message, onRetry = { viewModel.retry() })

            UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                val gameDetails = state.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                ) {
                    DetailTopBar(
                        gameDetails = gameDetails,
                    )

                    DetailContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        gameDetails = gameDetails,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailTopBar(
    modifier: Modifier = Modifier,
    gameDetails: GameDetails,
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        ImageWithOverlay(
            modifier = Modifier.fillMaxSize(),
            gameDetails.backgroundImageAdditional ?: gameDetails.backgroundImage,
        )

        Row(
            Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            TopBarDetail(
                modifier = Modifier.weight(0.6f),
                gameDetails = gameDetails,
            )

            Card(
                Modifier
                    .weight(0.4f)
                    .fillMaxHeight(),
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
private fun TopBarDetail(
    modifier: Modifier = Modifier,
    gameDetails: GameDetails,
) {
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        HeaderSection(gameDetails)

        Spacer(Modifier.height(15.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            PlaytimeText(gameDetails.playtime)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                MetacriticCard(gameDetails, context)
                RatingCard(gameDetails)
            }

            gameDetails.website?.let {
                WebsiteLink(it, context)
            }
        }

        Spacer(Modifier.height(10.dp))

        PlatformsRow(gameDetails.parentPlatforms)
    }
}

@Composable
private fun HeaderSection(gameDetails: GameDetails) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(4.dp),
        ) {
            gameDetails.released?.let {
                Text(
                    modifier = Modifier.padding(3.dp),
                    text = it,
                    color = Color.Black,
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = Fonts.ArialRounded,
                )
            }
        }

        gameDetails.name?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = it,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PlaytimeText(playtime: Int?) {
    Text(
        text = "Average Playtime: ${playtime ?: "N/A"}",
        color = Color.White,
        style = MaterialTheme.typography.labelLarge,
        fontFamily = Fonts.ArialRounded,
    )
}

@Composable
private fun MetacriticCard(gameDetails: GameDetails, context: Context) {
    Card(
        modifier = Modifier.noRippleClickable {
            gameDetails.metacriticUrl?.checkUrlValidation(
                onValidUrl = { openUrlInBrowser(context, it) },
                onUrlValidationError = {
                    context.showShortToast("Metacritic domain is not valid")
                },
            )
        },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(4.dp),
    ) {
        Row(
            modifier = Modifier.padding(3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_meta),
                contentDescription = "Metacritic Rate",
            )

            Text(
                text = "${gameDetails.metacritic ?: "N/A"}",
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun RatingCard(gameDetails: GameDetails) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = getColorFromRate(
                gameDetails.rating ?: 0.0,
                gameDetails.ratingTop?.toDouble() ?: 0.0,
            ),
        ),
        shape = RoundedCornerShape(4.dp),
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.padding(horizontal = 3.dp),
                painter = painterResource(R.drawable.ic_rate),
                contentDescription = null,
                tint = Color.White,
            )

            VerticalDivider(
                Modifier
                    .fillMaxHeight()
                    .padding(vertical = 3.dp),
                thickness = 1.5.dp,
                color = Color.White,
            )

            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "${gameDetails.rating ?: "N/A"}/${gameDetails.ratingTop ?: "N/A"}",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun WebsiteLink(website: String, context: Context) {
    Text(
        modifier = Modifier.noRippleClickable {
            website.checkUrlValidation(
                onValidUrl = { openUrlInBrowser(context, it) },
                onUrlValidationError = {
                    context.showShortToast("Store domain is not valid")
                },
            )
        },
        text = website,
        color = LightHyperlinkColor,
        style = MaterialTheme.typography.labelMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textDecoration = TextDecoration.Underline,
    )
}

@Composable
private fun PlatformsRow(platforms: List<ParentPlatform>) {
    val items = platforms.mapNotNull { it.platform }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 3.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        items(items) { platform ->
            PlatformIcon(platform)
        }
    }
}

@Composable
private fun DetailContent(
    modifier: Modifier = Modifier,
    gameDetails: GameDetails,
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        AboutSection(gameDetails.descriptionRaw)

        MetaSection(gameDetails)

        SimpleTextSection("Genres", gameDetails.genres.map { it.name })
        SimpleTextSection("Publisher", gameDetails.publishers.map { it.name })
        SimpleTextSection("Developer", gameDetails.developers.map { it.name })

        ClickableSection(
            "Platforms",
            gameDetails.platforms.map { it.platform?.name },
        ) {
            context.showShortToast("Platform : $it clicked")
        }

        StoresSection(gameDetails.stores)

        ClickableSection(
            "Tags",
            gameDetails.tags.map { it.name },
        ) {
            context.showShortToast("Tag : $it clicked")
        }
    }
}

@Composable
private fun AboutSection(description: String?) {
    description?.let {
        DetailSection("About") {
            ExpandableText(
                modifier = Modifier.fillMaxWidth(),
                text = it,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Justify,
                maxLines = 5,
                lineHeight = 18.sp,
            )
        }
    }
}

@Composable
private fun MetaSection(gameDetails: GameDetails) {
    Row(Modifier.fillMaxWidth()) {
        gameDetails.released?.let {
            DetailSection(modifier = Modifier.weight(1f), title = "Release Date") {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    lineHeight = 18.sp,
                )
            }
        }

        DetailSection(modifier = Modifier.weight(1f), title = "Age Rating") {
            AgeRatingContent(gameDetails)
        }
    }
}

@Composable
private fun AgeRatingContent(gameDetails: GameDetails) {
    ShowDefaultTextTooltip(
        tooltipText = gameDetails.esrbRating.description,
    ) { tooltipState, scope ->
        Row(
            modifier = Modifier.clickable {
                scope.launch { tooltipState.show() }
            },
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .height(25.dp)
                    .aspectRatio(0.75f),
                painter = painterResource(gameDetails.esrbRating.iconResId),
                contentScale = ContentScale.Fit,
                contentDescription = "ESRB Rating",
            )

            Text(
                text = gameDetails.esrbRating.title,
                style = MaterialTheme.typography.labelLarge,
                lineHeight = 18.sp,
            )
        }
    }
}

@Composable
private fun SimpleTextSection(title: String, items: List<String?>) {
    val text = items.mapNotNull { it }.joinToString(", ")
    if (text.isNotEmpty()) {
        DetailSection(title) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                style = MaterialTheme.typography.labelLarge,
                lineHeight = 18.sp,
            )
        }
    }
}

@Composable
private fun ClickableSection(
    title: String,
    items: List<String?>,
    onClick: (String) -> Unit,
) {
    val text = items.mapNotNull { it }.joinToString(", ")

    if (text.isNotEmpty()) {
        DetailSection(title) {
            ClickableWordsText(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                lineHeight = 18.sp,
                onWordClick = onClick,
            )
        }
    }
}

@Composable
private fun StoresSection(stores: List<StoreInfo>) {
    val validStores = stores.mapNotNull { it.store }

    if (validStores.isNotEmpty()) {
        DetailSection("Stores") {
            LazyRow {
                items(validStores) { store ->
                    StoreItem(store)
                }
            }
        }
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
        contentDescription = platform.name,
    )
}

@Composable
private fun StoreItem(store: Store) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .height(120.dp)
            .aspectRatio(1.7f)
            .padding(horizontal = 5.dp),
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
                    },
                )
            },
            backgroundContent = {
                ImageWithOverlay(
                    modifier = Modifier.fillMaxSize(),
                    imageUrl = store.imageBackground,
                )
            },
            foregroundContent = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        3.dp,
                        alignment = Alignment.CenterHorizontally,
                    ),
                ) {
                    StoreIcon(store)
                    Text(
                        text = store.name ?: "N/A",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                    )
                }
            },
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
        tint = Color.White,
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
