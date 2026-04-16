package com.faridev.gameradar.domain.usecase

import com.faridev.gameradar.domain.model.GameEntityDetails
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.domain.repository.GameRepository
import com.faridev.gameradar.presentation.common.state.UiState

class FetchEntityDetailsUseCase(private val gameRepository: GameRepository) {

    suspend operator fun invoke(
        type: GameEntityType,
        entityId: Int,
    ): UiState<GameEntityDetails> = gameRepository.fetchEntityDetails(type, entityId)
}
