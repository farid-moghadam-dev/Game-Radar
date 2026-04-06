package com.faridev.gameradar.domain.usecase

import androidx.paging.PagingData
import com.faridev.gameradar.domain.model.GameEntity
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow

class FetchEntityListUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(type: GameEntityType): Flow<PagingData<GameEntity>> =
        gameRepository.getEntityStream(type)
}
