package com.faridev.gameradar.data.mapper

import com.faridev.gameradar.data.model.GamesListResDto
import com.faridev.gameradar.data.model.ParentPlatformDto
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.domain.model.GamesList
import com.faridev.gameradar.domain.model.ParentPlatform
import com.faridev.gameradar.domain.model.ParentPlatformInfo

fun GamesListResDto.toDomain() = GamesList(
    count = count,
    next = next,
    previous = previous,
    results = results.map { it.toDomain() },
    seoTitle = seoTitle
)

private fun GamesListResDto.Result.toDomain() = GameResult(
    backgroundImage = backgroundImage,
    id = id,
    metacritic = metacritic,
    name = name,
    parentPlatforms = parentPlatforms.map { it.toDomain() }
)

fun ParentPlatformDto.toDomain() = ParentPlatform(
    platform = platform?.toDomain()
)

private fun ParentPlatformDto.Platform.toDomain() = ParentPlatformInfo(
    id = id,
    name = name,
    slug = slug
)