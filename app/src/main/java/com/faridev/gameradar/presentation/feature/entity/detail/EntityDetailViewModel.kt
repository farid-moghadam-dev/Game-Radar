package com.faridev.gameradar.presentation.feature.entity.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.faridev.gameradar.domain.model.GameEntityDetails
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.domain.usecase.FetchEntityDetailsUseCase
import com.faridev.gameradar.domain.usecase.FetchGamesByEntityUseCase
import com.faridev.gameradar.presentation.common.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class EntityDetailViewModel(
    private val fetchEntityDetails: FetchEntityDetailsUseCase,
    private val fetchGamesByEntity: FetchGamesByEntityUseCase
) : ViewModel() {

    var detailsState: UiState<GameEntityDetails> by mutableStateOf(UiState.Loading)
        private set

    private val request = MutableStateFlow<Pair<GameEntityType, Int>?>(null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val gamesFlow: Flow<PagingData<GameResult>> = request
        .flatMapLatest { req ->
            if (req == null) flowOf(PagingData.empty())
            else fetchGamesByEntity(req.first, req.second)
        }
        .cachedIn(viewModelScope)

    fun load(type: GameEntityType, entityId: Int) {
        if (request.value?.first == type && request.value?.second == entityId) return
        request.value = type to entityId
        viewModelScope.launch {
            detailsState = UiState.Loading
            detailsState = fetchEntityDetails(type, entityId)
        }
    }

    fun retry() {
        val current = request.value ?: return
        viewModelScope.launch {
            detailsState = UiState.Loading
            detailsState = fetchEntityDetails(current.first, current.second)
        }
    }
}
