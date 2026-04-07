package com.faridev.gameradar.data.mapper

import com.faridev.gameradar.data.model.GameEntityDetailsResDto
import com.faridev.gameradar.data.model.GameEntityListResDto
import com.faridev.gameradar.domain.model.GameEntity
import com.faridev.gameradar.domain.model.GameEntityDetails
import com.faridev.gameradar.domain.model.GameEntityGameRef
import com.faridev.gameradar.domain.model.GameEntityList

fun GameEntityListResDto.toDomain() = GameEntityList(
    count = count,
    next = next,
    previous = previous,
    results = results.map { it.toDomain() }
)

private fun GameEntityListResDto.Result.toDomain() = GameEntity(
    id = id,
    name = name,
    slug = slug,
    gamesCount = gamesCount,
    imageBackground = imageBackground,
    topGames = games.map { it.toDomain() }
)

private fun GameEntityListResDto.Result.Game.toDomain() = GameEntityGameRef(
    id = id,
    name = name,
    slug = slug
)

fun GameEntityDetailsResDto.toDomain() = GameEntityDetails(
    id = id,
    name = name,
    slug = slug,
    gamesCount = gamesCount,
    imageBackground = imageBackground,
    description = description,
    domain = domain
)
