package com.faridev.gameradar.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class GameEntityList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GameEntity>
)

@Immutable
data class GameEntity(
    val id: Int,
    val name: String,
    val slug: String?,
    val gamesCount: Int,
    val imageBackground: String?,
    val topGames: List<GameEntityGameRef> = emptyList()
)

@Immutable
data class GameEntityGameRef(
    val id: Int,
    val name: String,
    val slug: String?
)

@Immutable
data class GameEntityDetails(
    val id: Int,
    val name: String,
    val slug: String?,
    val gamesCount: Int,
    val imageBackground: String?,
    val description: String?,
    val domain: String?
)
