package com.faridev.gameradar.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class GamesList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GameResult>,
    val seoTitle: String?
)

@Immutable
data class GameResult(
    val backgroundImage: String?,
    val id: Int,
    val metacritic: Int?,
    val name: String,
    val parentPlatforms: List<ParentPlatform> = emptyList()
)