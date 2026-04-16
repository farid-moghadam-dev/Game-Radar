package com.faridev.gameradar.domain.usecase

import androidx.paging.PagingData
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow

class FetchGamesListUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(): Flow<PagingData<GameResult>> =
        gameRepository.getGamesStream()
}
