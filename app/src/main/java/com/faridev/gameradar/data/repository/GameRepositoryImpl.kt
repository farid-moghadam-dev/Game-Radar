package com.faridev.gameradar.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.faridev.gameradar.data.mapper.toDomain
import com.faridev.gameradar.data.model.GameDetailsResDto
import com.faridev.gameradar.data.model.GameEntityDetailsResDto
import com.faridev.gameradar.data.model.GameEntityListResDto
import com.faridev.gameradar.data.model.GamesListResDto
import com.faridev.gameradar.data.paging.source.GameEntityPagingSource
import com.faridev.gameradar.data.paging.source.GamesListPagingSource
import com.faridev.gameradar.data.remote.GameApi
import com.faridev.gameradar.data.util.safeApiCall
import com.faridev.gameradar.domain.model.GameDetails
import com.faridev.gameradar.domain.model.GameEntity
import com.faridev.gameradar.domain.model.GameEntityDetails
import com.faridev.gameradar.domain.model.GameEntityList
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.domain.model.GamesList
import com.faridev.gameradar.domain.repository.GameRepository
import com.faridev.gameradar.presentation.common.state.UiState
import io.ktor.client.call.body
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow

class GameRepositoryImpl(private val gameApi: GameApi) : GameRepository {

    override fun getGamesStream(): Flow<PagingData<GameResult>> =
        Pager(
            config = PagingConfig(pageSize = 40),
            pagingSourceFactory = {
                GamesListPagingSource { page, pageSize -> fetchGamesList(page, pageSize) }
            },
        ).flow

    override suspend fun fetchGamesList(page: Int, pageSize: Int): UiState<GamesList> =
        safeApiCall {
            val response = gameApi.fetchGamesList(page, pageSize)

            if (!response.status.isSuccess()) {
                throw ServerResponseException(response, "HTTP ${response.status.value}")
            }

            response.body<GamesListResDto>().toDomain()
        }

    override suspend fun fetchGameDetails(gameId: Int): UiState<GameDetails> =
        safeApiCall {
            val response = gameApi.fetchGameDetails(gameId)
            if (!response.status.isSuccess()) {
                throw ServerResponseException(response, "HTTP ${response.status.value}")
            }

            response.body<GameDetailsResDto>().toDomain()
        }

    override fun getEntityStream(type: GameEntityType): Flow<PagingData<GameEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                GameEntityPagingSource { page, pageSize -> fetchEntityList(type, page, pageSize) }
            },
        ).flow

    override suspend fun fetchEntityList(
        type: GameEntityType,
        page: Int,
        pageSize: Int,
    ): UiState<GameEntityList> =
        safeApiCall {
            val response = gameApi.fetchEntityList(type, page, pageSize)
            if (!response.status.isSuccess()) {
                throw ServerResponseException(response, "HTTP ${response.status.value}")
            }

            response.body<GameEntityListResDto>().toDomain()
        }

    override suspend fun fetchEntityDetails(
        type: GameEntityType,
        entityId: Int,
    ): UiState<GameEntityDetails> =
        safeApiCall {
            val response = gameApi.fetchEntityDetails(type, entityId)
            if (!response.status.isSuccess()) {
                throw ServerResponseException(response, "HTTP ${response.status.value}")
            }

            response.body<GameEntityDetailsResDto>().toDomain()
        }

    override fun getGamesByEntityStream(
        type: GameEntityType,
        entityId: Int,
    ): Flow<PagingData<GameResult>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                GamesListPagingSource { page, pageSize ->
                    safeApiCall {
                        val response = gameApi.fetchGamesByEntity(type, entityId, page, pageSize)

                        if (!response.status.isSuccess()) {
                            throw ServerResponseException(response, "HTTP ${response.status.value}")
                        }

                        response.body<GamesListResDto>().toDomain()
                    }
                }
            },
        ).flow
}
