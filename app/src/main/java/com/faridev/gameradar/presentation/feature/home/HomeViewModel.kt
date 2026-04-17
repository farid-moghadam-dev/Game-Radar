package com.faridev.gameradar.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.domain.usecase.FetchGamesListUseCase
import kotlinx.coroutines.flow.Flow

class HomeViewModel(fetchGamesList: FetchGamesListUseCase) : ViewModel() {

    val gamesFlow: Flow<PagingData<GameResult>> =
        fetchGamesList().cachedIn(viewModelScope) // Cache data to survive configuration changes
}
