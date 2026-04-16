package com.faridev.gameradar.data.paging.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faridev.gameradar.domain.model.GameEntity
import com.faridev.gameradar.domain.model.GameEntityList
import com.faridev.gameradar.presentation.common.state.UiState
import kotlin.coroutines.cancellation.CancellationException

class GameEntityPagingSource(
    private val entityListApiCall: suspend (page: Int, pageSize: Int) -> UiState<GameEntityList>,
) : PagingSource<Int, GameEntity>() {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GameEntity> = try {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        val responseUiState = entityListApiCall(page, pageSize)
        when (responseUiState) {
            is UiState.Error -> LoadResult.Error(Exception(responseUiState.message))
            UiState.Loading -> LoadResult.Invalid()
            is UiState.Success<GameEntityList> -> {
                val responseData = responseUiState.data
                LoadResult.Page(
                    data = responseData.results,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (responseData.next == null) null else page + 1,
                )
            }
        }
    } catch (e: CancellationException) {
        throw e
    } catch (exception: Exception) {
        LoadResult.Error(exception)
    }

    override fun getRefreshKey(state: PagingState<Int, GameEntity>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}
