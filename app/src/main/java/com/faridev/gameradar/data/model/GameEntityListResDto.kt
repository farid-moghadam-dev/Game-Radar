package com.faridev.gameradar.data.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class GameEntityListResDto(
    @SerialName("count") val count: Int = 0,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<Result> = emptyList()
) {
    @Serializable
    @JsonIgnoreUnknownKeys
    data class Result(
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String,
        @SerialName("slug") val slug: String? = null,
        @SerialName("games_count") val gamesCount: Int = 0,
        @SerialName("image_background") val imageBackground: String? = null,
        @SerialName("games") val games: List<Game> = emptyList()
    ) {
        @Serializable
        @JsonIgnoreUnknownKeys
        data class Game(
            @SerialName("id") val id: Int,
            @SerialName("name") val name: String,
            @SerialName("slug") val slug: String? = null
        )
    }
}
