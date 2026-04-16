package com.faridev.gameradar.domain.usecase

import com.faridev.gameradar.domain.model.GameDetails
import com.faridev.gameradar.domain.repository.GameRepository
import com.faridev.gameradar.presentation.common.state.UiState

class FetchGameDetailsUseCase(private val gameRepository: GameRepository) {

    suspend operator fun invoke(gameId: Int): UiState<GameDetails> = gameRepository.fetchGameDetails(gameId)
}
