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
    private val fetchGameDetailsUseCase: FetchGameDetailsUseCase
) : ViewModel() {

    var uiState: UiState<GameDetails> by mutableStateOf(UiState.Loading)
        private set


    fun fetchGameDetails(gameId: Int) {
        viewModelScope.launch {
            uiState = UiState.Loading
            uiState = fetchGameDetailsUseCase.invoke(gameId)
        }
    }
}