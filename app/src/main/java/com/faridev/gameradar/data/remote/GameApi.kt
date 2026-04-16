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
import io.ktor.client.statement.HttpResponse

class GameApi(private val client: HttpClient) {

    suspend fun fetchGamesList(page: Int, pageSize: Int): HttpResponse =
        client.get("games") {
            parameter("page", page)
            parameter("page_size", pageSize)
        }

    suspend fun fetchGameDetails(gameId: Int): HttpResponse =
        client.get("games/$gameId").body()

    suspend fun fetchEntityList(
        type: GameEntityType,
        page: Int,
        pageSize: Int,
    ): HttpResponse =
        client.get(type.apiPath) {
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()

    suspend fun fetchEntityDetails(
        type: GameEntityType,
        entityId: Int,
    ): HttpResponse =
        client.get("${type.apiPath}/$entityId").body()

    suspend fun fetchGamesByEntity(
        type: GameEntityType,
        entityId: Int,
        page: Int,
        pageSize: Int,
    ): HttpResponse =
        client.get("games") {
            parameter(type.gamesFilterParam, entityId)
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()
}
