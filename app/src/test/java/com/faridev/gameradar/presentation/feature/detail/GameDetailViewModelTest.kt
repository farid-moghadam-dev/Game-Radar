package com.faridev.gameradar.presentation.feature.detail

import androidx.paging.PagingData
import com.faridev.gameradar.domain.model.AppError
import com.faridev.gameradar.domain.model.EsrbRating
import com.faridev.gameradar.domain.model.GameDetails
import com.faridev.gameradar.domain.model.GameEntity
import com.faridev.gameradar.domain.model.GameEntityDetails
import com.faridev.gameradar.domain.model.GameEntityList
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.domain.model.GameResult
import com.faridev.gameradar.domain.model.GamesList
import com.faridev.gameradar.domain.repository.GameRepository
import com.faridev.gameradar.domain.usecase.FetchGameDetailsUseCase
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

@OptIn(ExperimentalCoroutinesApi::class)
class GameDetailViewModelTest {

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
    fun `initial state is Loading`() {
        val vm = GameDetailViewModel(
            FetchGameDetailsUseCase(FakeRepo(UiState.Success(fakeGameDetails(1, "x"))))
        )
        assertEquals(UiState.Loading, vm.detailsState)
    }

    @Test
    fun `load moves state to Success`() = runTest(dispatcher) {
        val expected = fakeGameDetails(id = 42, name = "Half-Life 3")
        val vm = GameDetailViewModel(
            FetchGameDetailsUseCase(FakeRepo(UiState.Success(expected)))
        )

        vm.load(gameId = 42)
        dispatcher.scheduler.advanceUntilIdle()

        val state = vm.detailsState
        assertTrue(state is UiState.Success)
        assertEquals("Half-Life 3", (state as UiState.Success).data.name)
    }

    @Test
    fun `load surfaces repository errors`() = runTest(dispatcher) {
        val vm = GameDetailViewModel(
            FetchGameDetailsUseCase(FakeRepo(UiState.Error(AppError.NoConnection)))
        )

        vm.load(gameId = 1)
        dispatcher.scheduler.advanceUntilIdle()

        val state = vm.detailsState
        assertTrue(state is UiState.Error)
        assertEquals(AppError.NoConnection, (state as UiState.Error).error)
    }

    @Test
    fun `retry re-invokes details fetch after an error`() = runTest(dispatcher) {
        val repo = FakeRepo(UiState.Error(AppError.Timeout))
        val vm = GameDetailViewModel(FetchGameDetailsUseCase(repo))

        vm.load(gameId = 7)
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(vm.detailsState is UiState.Error)

        repo.detailsResult = UiState.Success(fakeGameDetails(id = 7, name = "Portal 3"))
        vm.retry()
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(vm.detailsState is UiState.Success)
        assertEquals("Portal 3", (vm.detailsState as UiState.Success).data.name)
    }

    // -- Fakes ----------------------------------------------------------------

    private class FakeRepo(
        var detailsResult: UiState<GameDetails>
    ) : GameRepository {
        override fun getGamesStream(): Flow<PagingData<GameResult>> = flowOf(PagingData.empty())
        override suspend fun fetchGamesList(page: Int, pageSize: Int): UiState<GamesList> =
            UiState.Success(GamesList(0, null, null, emptyList(), null))
        override suspend fun fetchGameDetails(gameId: Int): UiState<GameDetails> = detailsResult
        override fun getEntityStream(type: GameEntityType): Flow<PagingData<GameEntity>> =
            flowOf(PagingData.empty())
        override suspend fun fetchEntityList(
            type: GameEntityType,
            page: Int,
            pageSize: Int
        ): UiState<GameEntityList> = UiState.Success(GameEntityList(0, null, null, emptyList()))
        override suspend fun fetchEntityDetails(
            type: GameEntityType,
            entityId: Int
        ): UiState<GameEntityDetails> = UiState.Success(
            GameEntityDetails(0, "", null, 0, null, null, null)
        )
        override fun getGamesByEntityStream(
            type: GameEntityType,
            entityId: Int
        ): Flow<PagingData<GameResult>> = flowOf(PagingData.empty())
    }

    private fun fakeGameDetails(id: Int, name: String) = GameDetails(
        id = id,
        name = name,
        achievementsCount = null,
        backgroundImage = null,
        backgroundImageAdditional = null,
        descriptionRaw = null,
        developers = emptyList(),
        esrbRating = EsrbRating.RATING_PENDING,
        gameSeriesCount = null,
        genres = emptyList(),
        metacritic = null,
        metacriticUrl = null,
        nameOriginal = null,
        parentPlatforms = emptyList(),
        platforms = emptyList(),
        playtime = null,
        publishers = emptyList(),
        rating = null,
        ratingTop = null,
        released = null,
        slug = null,
        stores = emptyList(),
        tags = emptyList(),
        website = null
    )
}
