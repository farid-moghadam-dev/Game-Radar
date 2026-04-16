package com.faridev.gameradar.domain.repository

import androidx.paging.PagingData
import com.faridev.gameradar.domain.model.GameDetails
import com.faridev.gameradar.domain.model.GameEntity
import com.faridev.gameradar.domain.model.GameEntityDetails
import com.faridev.gameradar.domain.model.GameEntityList
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.domain.model.GamesList
import com.faridev.gameradar.presentation.common.state.UiState
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun getGamesStream(): Flow<PagingData<GameResult>>
    suspend fun fetchGamesList(page: Int, pageSize: Int = 40): UiState<GamesList>
    suspend fun fetchGameDetails(gameId: Int): UiState<GameDetails>

    fun getEntityStream(type: GameEntityType): Flow<PagingData<GameEntity>>
    suspend fun fetchEntityList(
        type: GameEntityType,
        page: Int,
        pageSize: Int = 20,
    ): UiState<GameEntityList>

    suspend fun fetchEntityDetails(
        type: GameEntityType,
        entityId: Int,
    ): UiState<GameEntityDetails>

    fun getGamesByEntityStream(
        type: GameEntityType,
        entityId: Int,
    ): Flow<PagingData<GameResult>>
}
