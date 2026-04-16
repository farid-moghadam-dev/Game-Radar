package com.faridev.gameradar.data.remote

import com.faridev.gameradar.data.model.GameDetailsResDto
import com.faridev.gameradar.data.model.GameEntityDetailsResDto
import com.faridev.gameradar.data.model.GameEntityListResDto
import com.faridev.gameradar.data.model.GamesListResDto
import com.faridev.gameradar.domain.model.GameEntityType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class GameApi(private val client: HttpClient) {

    suspend fun fetchGamesList(page: Int, pageSize: Int): GamesListResDto =
        client.get("games") {
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()

    suspend fun fetchGameDetails(gameId: Int): GameDetailsResDto =
        client.get("games/$gameId").body()

    suspend fun fetchEntityList(
        type: GameEntityType,
        page: Int,
        pageSize: Int,
    ): GameEntityListResDto =
        client.get(type.apiPath) {
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()

    suspend fun fetchEntityDetails(
        type: GameEntityType,
        entityId: Int,
    ): GameEntityDetailsResDto =
        client.get("${type.apiPath}/$entityId").body()

    suspend fun fetchGamesByEntity(
        type: GameEntityType,
        entityId: Int,
        page: Int,
        pageSize: Int,
    ): GamesListResDto =
        client.get("games") {
            parameter(type.gamesFilterParam, entityId)
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()
}
