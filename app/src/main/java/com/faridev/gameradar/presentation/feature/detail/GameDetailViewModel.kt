package com.faridev.gameradar.presentation.feature.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faridev.gameradar.domain.model.GameDetails
import com.faridev.gameradar.domain.usecase.FetchGameDetailsUseCase
import com.faridev.gameradar.presentation.common.state.UiState
import kotlinx.coroutines.launch

class GameDetailViewModel(
    private val fetchGameDetailsUseCase: FetchGameDetailsUseCase,
) : ViewModel() {

    var detailsState: UiState<GameDetails> by mutableStateOf(UiState.Loading)
        private set

    private var currentGameId: Int? = null

    fun load(gameId: Int) {
        if (currentGameId == gameId && detailsState is UiState.Success) return
        currentGameId = gameId
        fetchDetails(gameId)
    }

    fun retry() {
        val id = currentGameId ?: return
        fetchDetails(id)
    }

    private fun fetchDetails(gameId: Int) {
        viewModelScope.launch {
            detailsState = UiState.Loading
            detailsState = fetchGameDetailsUseCase.invoke(gameId)
        }
    }
}
