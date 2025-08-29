package com.faridev.gameradar.data.mapper

import com.faridev.gameradar.data.model.GameDetailsResDto
import com.faridev.gameradar.domain.model.Developer
import com.faridev.gameradar.domain.model.EsrbRating
import com.faridev.gameradar.domain.model.GameDetails
import com.faridev.gameradar.domain.model.Genre
import com.faridev.gameradar.domain.model.Platform
import com.faridev.gameradar.domain.model.PlatformInfo
import com.faridev.gameradar.domain.model.Publisher
import com.faridev.gameradar.domain.model.Requirements
import com.faridev.gameradar.domain.model.Store
import com.faridev.gameradar.domain.model.StoreInfo
import com.faridev.gameradar.domain.model.Tag
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.collections.map

fun GameDetailsResDto.toDomain(): GameDetails = GameDetails(
    id = id,
    name = name,
    achievementsCount = achievementsCount,
    backgroundImage = backgroundImage,
    backgroundImageAdditional = backgroundImageAdditional,
    descriptionRaw = descriptionRaw,
    developers = developers.map { it.toDomain() },
    esrbRating = esrbRating?.toDomain() ?: EsrbRating.RATING_PENDING,
    gameSeriesCount = gameSeriesCount,
    genres = genres.map { it.toDomain() },
    metacritic = metacritic,
    metacriticUrl = metacriticUrl,
    nameOriginal = nameOriginal,
    parentPlatforms = parentPlatforms.map { it.toDomain() },
    platforms = platforms.map { it.toDomain() },
    playtime = playtime,
    publishers = publishers.map { it.toDomain() },
    rating = rating,
    ratingTop = ratingTop,
    released = released?.formatReleaseDate(),
    slug = slug,
    stores = stores.map { it.toDomain() },
    tags = tags.map { it.toDomain() },
    website = website
)

private fun GameDetailsResDto.Developer.toDomain() = Developer(
    id = id,
    name = name,
    slug = slug
)

private fun GameDetailsResDto.EsrbRating.toDomain() =
    EsrbRating.getEsrbRatingBySlug(slug = slug)

private fun GameDetailsResDto.Genre.toDomain() = Genre(
    gamesCount = gamesCount,
    id = id,
    imageBackground = imageBackground,
    name = name,
    slug = slug
)

private fun GameDetailsResDto.Platform.toDomain() = PlatformInfo(
    platform = platform?.toDomain(),
    releasedAt = releasedAt,
    requirements = requirements?.toDomain()
)

private fun GameDetailsResDto.Platform.Platform.toDomain() = Platform(
    gamesCount = gamesCount,
    id = id,
    image = image,
    imageBackground = imageBackground,
    name = name,
    slug = slug,
    yearEnd = yearEnd,
    yearStart = yearStart
)

private fun GameDetailsResDto.Platform.Requirements.toDomain() =
    Requirements(
        minimum = minimum,
        recommended = recommended
    )

private fun GameDetailsResDto.Publisher.toDomain() = Publisher(
    gamesCount = gamesCount,
    id = id,
    imageBackground = imageBackground,
    name = name,
    slug = slug
)

private fun GameDetailsResDto.Store.toDomain(): StoreInfo = StoreInfo(
    store = store?.toDomain(),
    id = id,
    url = url
)

private fun GameDetailsResDto.Store.Store.toDomain() = Store(
    domain = domain,
    gamesCount = gamesCount,
    id = id,
    imageBackground = imageBackground,
    name = name,
    slug = slug
)

private fun GameDetailsResDto.Tag.toDomain(): Tag = Tag(
    id = id,
    name = name,
    slug = slug,
    language = language,
    gamesCount = gamesCount,
    imageBackground = imageBackground
)

private fun String.formatReleaseDate(): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val date = parser.parse(this)
        if (date != null) formatter.format(date) else this
    } catch (_: Exception) {
        this
    }
}