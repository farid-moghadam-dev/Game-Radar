package com.faridev.gameradar.data.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class GameEntityDetailsResDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String? = null,
    @SerialName("games_count") val gamesCount: Int = 0,
    @SerialName("image_background") val imageBackground: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("domain") val domain: String? = null,
)
