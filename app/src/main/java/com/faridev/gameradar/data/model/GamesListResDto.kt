package com.faridev.gameradar.data.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class GamesListResDto(
    @SerialName("count")
    val count: Int = 0,
    @SerialName("next")
    val next: String? = null,
    @SerialName("previous")
    val previous: String? = null,
    @SerialName("results")
    val results: List<Result> = emptyList(),
    @SerialName("seo_title")
    val seoTitle: String? = null
) {
    @Serializable
    @JsonIgnoreUnknownKeys
    data class Result(
        @SerialName("background_image")
        val backgroundImage: String? = null,
        @SerialName("id")
        val id: Int,
        @SerialName("metacritic")
        val metacritic: Int? = null,
        @SerialName("name")
        val name: String,
        @SerialName("parent_platforms")
        val parentPlatforms: List<ParentPlatformDto> = emptyList(),
    )
}