package com.faridev.gameradar.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.faridev.gameradar.data.mapper.toDomain
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
        safeApiCall { gameApi.fetchGamesList(page, pageSize).toDomain() }

    override suspend fun fetchGameDetails(gameId: Int): UiState<GameDetails> =
        safeApiCall { gameApi.fetchGameDetails(gameId).toDomain() }

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
        safeApiCall { gameApi.fetchEntityList(type, page, pageSize).toDomain() }

    override suspend fun fetchEntityDetails(
        type: GameEntityType,
        entityId: Int,
    ): UiState<GameEntityDetails> =
        safeApiCall { gameApi.fetchEntityDetails(type, entityId).toDomain() }

    override fun getGamesByEntityStream(
        type: GameEntityType,
        entityId: Int,
    ): Flow<PagingData<GameResult>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                GamesListPagingSource { page, pageSize ->
                    safeApiCall {
                        gameApi.fetchGamesByEntity(type, entityId, page, pageSize).toDomain()
                    }
                }
            },
        ).flow
}
