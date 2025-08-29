package com.faridev.gameradar.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class GameDetails(
    val id: Int?,
    val name: String?,
    val achievementsCount: Int?,
    val backgroundImage: String?,
    val backgroundImageAdditional: String?,
    val descriptionRaw: String?,
    val developers: List<Developer> = emptyList(),
    val esrbRating: EsrbRating,
    val gameSeriesCount: Int?,
    val genres: List<Genre> = emptyList(),
    val metacritic: Int?,
    val metacriticUrl: String?,
    val nameOriginal: String?,
    val parentPlatforms: List<ParentPlatform> = emptyList(),
    val platforms: List<PlatformInfo> = emptyList(),
    val playtime: Int?,
    val publishers: List<Publisher> = emptyList(),
    val rating: Double?,
    val ratingTop: Int?,
    val released: String?,
    val slug: String?,
    val stores: List<StoreInfo> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val website: String?,
)

@Immutable
data class Developer(
    val id: Int?,
    val name: String?,
    val slug: String?,
)

@Immutable
data class Genre(
    val gamesCount: Int?,
    val id: Int?,
    val imageBackground: String?,
    val name: String?,
    val slug: String?
)

@Immutable
data class PlatformInfo(
    val platform: Platform?,
    val releasedAt: String?,
    val requirements: Requirements?
)

@Immutable
data class Platform(
    val gamesCount: Int?,
    val id: Int?,
    val image: String?,
    val imageBackground: String?,
    val name: String?,
    val slug: String?,
    val yearEnd: Int?,
    val yearStart: Int?
)

@Immutable
data class Requirements(
    val minimum: String?,
    val recommended: String?
)

@Immutable
data class Publisher(
    val gamesCount: Int?,
    val id: Int?,
    val imageBackground: String?,
    val name: String?,
    val slug: String?
)

@Immutable
data class StoreInfo(
    val store: Store?,
    val id: Int?,
    val url: String?
)

@Immutable
data class Store(
    val domain: String?,
    val gamesCount: Int?,
    val id: Int?,
    val imageBackground: String?,
    val name: String?,
    val slug: String?
)

@Immutable
data class Tag(
    val gamesCount: Int?,
    val id: Int?,
    val imageBackground: String?,
    val language: String?,
    val name: String?,
    val slug: String?
)