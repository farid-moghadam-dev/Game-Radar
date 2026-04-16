package com.faridev.gameradar.domain.usecase

import androidx.paging.PagingData
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow

class FetchGamesByEntityUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(
        type: GameEntityType,
        entityId: Int,
    ): Flow<PagingData<GameResult>> =
        gameRepository.getGamesByEntityStream(type, entityId)
}
