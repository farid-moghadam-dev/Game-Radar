package com.faridev.gameradar.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentPlatformDto(
    @SerialName("platform")
    val platform: Platform? = null
) {
    @Serializable
    data class Platform(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("slug")
        val slug: String? = null
    )
}