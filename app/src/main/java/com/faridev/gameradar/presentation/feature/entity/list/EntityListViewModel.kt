package com.faridev.gameradar.presentation.feature.entity.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.faridev.gameradar.domain.model.GameEntity
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.domain.usecase.FetchEntityListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class EntityListViewModel(
    private val fetchEntityList: FetchEntityListUseCase,
) : ViewModel() {

    private val typeFlow = MutableStateFlow<GameEntityType?>(null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val entitiesFlow: Flow<PagingData<GameEntity>> = typeFlow
        .flatMapLatest { type ->
            if (type == null) flowOf(PagingData.empty()) else fetchEntityList(type)
        }
        .cachedIn(viewModelScope)

    fun setType(type: GameEntityType) {
        if (typeFlow.value != type) typeFlow.value = type
    }
}
