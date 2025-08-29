package com.faridev.gameradar.data.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class GameDetailsResDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("achievements_count")
    val achievementsCount: Int? = null,
    @SerialName("background_image")
    val backgroundImage: String? = null,
    @SerialName("background_image_additional")
    val backgroundImageAdditional: String? = null,
    @SerialName("description_raw")
    val descriptionRaw: String? = null,
    @SerialName("developers")
    val developers: List<Developer> = emptyList(),
    @SerialName("esrb_rating")
    val esrbRating: EsrbRating? = null,
    @SerialName("game_series_count")
    val gameSeriesCount: Int? = null,
    @SerialName("genres")
    val genres: List<Genre> = emptyList(),
    @SerialName("metacritic")
    val metacritic: Int? = null,
    @SerialName("metacritic_url")
    val metacriticUrl: String? = null,
    @SerialName("name_original")
    val nameOriginal: String? = null,
    @SerialName("parent_platforms")
    val parentPlatforms: List<ParentPlatformDto> = emptyList(),
    @SerialName("platforms")
    val platforms: List<Platform> = emptyList(),
    @SerialName("playtime")
    val playtime: Int? = null,
    @SerialName("publishers")
    val publishers: List<Publisher> = emptyList(),
    @SerialName("rating")
    val rating: Double? = null,
    @SerialName("rating_top")
    val ratingTop: Int? = null,
    @SerialName("released")
    val released: String? = null,
    @SerialName("slug")
    val slug: String? = null,
    @SerialName("stores")
    val stores: List<Store> = emptyList(),
    @SerialName("tags")
    val tags: List<Tag> = emptyList(),
    @SerialName("website")
    val website: String? = null,
) {
    @Serializable
    @JsonIgnoreUnknownKeys
    data class Developer(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("slug")
        val slug: String? = null
    )

    @Serializable
    @JsonIgnoreUnknownKeys
    data class EsrbRating(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("slug")
        val slug: String? = null
    )

    @Serializable
    @JsonIgnoreUnknownKeys
    data class Genre(
        @SerialName("games_count")
        val gamesCount: Int? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("image_background")
        val imageBackground: String? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("slug")
        val slug: String? = null
    )

    @Serializable
    @JsonIgnoreUnknownKeys
    data class Platform(
        @SerialName("platform")
        val platform: Platform? = null,
        @SerialName("released_at")
        val releasedAt: String? = null,
        @SerialName("requirements")
        val requirements: Requirements? = null
    ) {
        @Serializable
        @JsonIgnoreUnknownKeys
        data class Platform(
            @SerialName("games_count")
            val gamesCount: Int? = null,
            @SerialName("id")
            val id: Int? = null,
            @SerialName("image")
            val image: String? = null,
            @SerialName("image_background")
            val imageBackground: String? = null,
            @SerialName("name")
            val name: String? = null,
            @SerialName("slug")
            val slug: String? = null,
            @SerialName("year_end")
            val yearEnd: Int? = null,
            @SerialName("year_start")
            val yearStart: Int? = null
        )

        @Serializable
        @JsonIgnoreUnknownKeys
        data class Requirements(
            @SerialName("minimum")
            val minimum: String? = null,
            @SerialName("recommended")
            val recommended: String? = null
        )
    }

    @Serializable
    @JsonIgnoreUnknownKeys
    data class Publisher(
        @SerialName("games_count")
        val gamesCount: Int? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("image_background")
        val imageBackground: String? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("slug")
        val slug: String? = null
    )

    @Serializable
    @JsonIgnoreUnknownKeys
    data class Store(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("store")
        val store: Store? = null,
        @SerialName("url")
        val url: String? = null
    ) {
        @Serializable
        @JsonIgnoreUnknownKeys
        data class Store(
            @SerialName("domain")
            val domain: String? = null,
            @SerialName("games_count")
            val gamesCount: Int? = null,
            @SerialName("id")
            val id: Int? = null,
            @SerialName("image_background")
            val imageBackground: String? = null,
            @SerialName("name")
            val name: String? = null,
            @SerialName("slug")
            val slug: String? = null
        )
    }

    @Serializable
    @JsonIgnoreUnknownKeys
    data class Tag(
        @SerialName("games_count")
        val gamesCount: Int? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("image_background")
        val imageBackground: String? = null,
        @SerialName("language")
        val language: String? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("slug")
        val slug: String? = null
    )
}