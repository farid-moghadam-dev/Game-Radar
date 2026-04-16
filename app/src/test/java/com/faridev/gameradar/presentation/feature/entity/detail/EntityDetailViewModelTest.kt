package com.faridev.gameradar.presentation.feature.entity.detail

import androidx.paging.PagingData
import app.cash.turbine.test
import com.faridev.gameradar.domain.model.AppError
import com.faridev.gameradar.domain.model.GameDetails
import com.faridev.gameradar.domain.model.GameEntity
import com.faridev.gameradar.domain.model.GameEntityDetails
import com.faridev.gameradar.domain.model.GameEntityList
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.domain.model.GamesList
import com.faridev.gameradar.domain.repository.GameRepository
import com.faridev.gameradar.domain.usecase.FetchEntityDetailsUseCase
import com.faridev.gameradar.domain.usecase.FetchGamesByEntityUseCase
import com.faridev.gameradar.presentation.common.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class EntityDetailViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load emits Success details and publishes paging data`() = runTest(dispatcher) {
        val details = GameEntityDetails(
            id = 1612,
            name = "Valve Software",
            slug = "valve-software",
            gamesCount = 44,
            imageBackground = null,
            description = null,
            domain = null,
        )
        val repo = FakeRepo(
            detailsResult = UiState.Success(details),
        )
        val vm = EntityDetailViewModel(
            FetchEntityDetailsUseCase(repo),
            FetchGamesByEntityUseCase(repo),
        )

        vm.gamesFlow.test(timeout = 3.seconds) {
            // Initial empty page emitted before load is called.
            awaitItem()

            vm.load(GameEntityType.Developers, entityId = 1612)
            dispatcher.scheduler.advanceUntilIdle()

            // After load, a PagingData is emitted for the new request.
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        assertTrue(vm.detailsState is UiState.Success)
        assertEquals("Valve Software", (vm.detailsState as UiState.Success).data.name)
    }

    @Test
    fun `retry re-invokes details fetch after an error`() = runTest(dispatcher) {
        val repo = FakeRepo(detailsResult = UiState.Error(AppError.Timeout))
        val vm = EntityDetailViewModel(
            FetchEntityDetailsUseCase(repo),
            FetchGamesByEntityUseCase(repo),
        )

        vm.load(GameEntityType.Genres, entityId = 4)
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(vm.detailsState is UiState.Error)

        repo.detailsResult = UiState.Success(
            GameEntityDetails(4, "Action", "action", 100, null, null, null),
        )
        vm.retry()
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(vm.detailsState is UiState.Success)
        assertEquals("Action", (vm.detailsState as UiState.Success).data.name)
    }

    // -- Fakes ----------------------------------------------------------------

    private class FakeRepo(
        var detailsResult: UiState<GameEntityDetails>,
    ) : GameRepository {
        override fun getGamesStream(): Flow<PagingData<GameResult>> = flowOf(PagingData.empty())
        override suspend fun fetchGamesList(page: Int, pageSize: Int): UiState<GamesList> =
            UiState.Success(GamesList(0, null, null, emptyList(), null))
        override suspend fun fetchGameDetails(gameId: Int): UiState<GameDetails> =
            UiState.Error(AppError.Unknown())
        override fun getEntityStream(type: GameEntityType): Flow<PagingData<GameEntity>> =
            flowOf(PagingData.empty())
        override suspend fun fetchEntityList(
            type: GameEntityType,
            page: Int,
            pageSize: Int,
        ): UiState<GameEntityList> = UiState.Success(GameEntityList(0, null, null, emptyList()))
        override suspend fun fetchEntityDetails(
            type: GameEntityType,
            entityId: Int,
        ): UiState<GameEntityDetails> = detailsResult
        override fun getGamesByEntityStream(
            type: GameEntityType,
            entityId: Int,
        ): Flow<PagingData<GameResult>> = flowOf(PagingData.empty())
    }
}
