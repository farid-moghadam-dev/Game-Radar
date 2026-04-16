package com.faridev.gameradar.data.paging.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.domain.model.GamesList
import com.faridev.gameradar.presentation.common.state.UiState

class GamesListPagingSource(
    private val gamesListApiCall: suspend (page: Int, pageSize: Int) -> UiState<GamesList>,
) : PagingSource<Int, GameResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GameResult> = try {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        val responseUiState = gamesListApiCall(page, pageSize)
        when (responseUiState) {
            is UiState.Error -> LoadResult.Error(Exception(responseUiState.message))
            UiState.Loading -> LoadResult.Invalid()
            is UiState.Success<GamesList> -> {
                val responseData = responseUiState.data
                LoadResult.Page(
                    data = responseData.results,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (responseData.next == null) null else page + 1,
                )
            }
        }
    } catch (exception: Exception) {
        LoadResult.Error(exception)
    }

    override fun getRefreshKey(state: PagingState<Int, GameResult>): Int? = state.anchorPosition?.let { anchorPosition ->
        state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
    }
}
