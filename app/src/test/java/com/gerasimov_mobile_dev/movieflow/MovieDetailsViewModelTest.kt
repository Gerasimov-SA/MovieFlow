package com.gerasimov_mobile_dev.movieflow

import androidx.lifecycle.SavedStateHandle
import com.gerasimov_mobile_dev.movieflow.fake.FakeMoviesRepository
import com.gerasimov_mobile_dev.movieflow.fake.testMovie
import com.gerasimov_mobile_dev.movieflow.presentation.MovieDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeMoviesRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeMoviesRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModelWithId(movieId: Int): MovieDetailsViewModel =
        MovieDetailsViewModel(
            repository = fakeRepository,
            savedStateHandle = SavedStateHandle(mapOf("movieId" to movieId))
        )

    @Test
    fun `movie details loaded successfully`() = runTest(testDispatcher) {
        fakeRepository.movieDetailsToReturn = testMovie(id = 123, name = "Inception")

        val viewModel = viewModelWithId(123)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.movie)
        assertEquals("Inception", state.movie?.name)
        assertEquals(123, state.movie?.id)
        assertEquals(false, state.isLoading)
        assertNull(state.error)
        assertEquals(1, fakeRepository.getMovieDetailsCallCount)
    }

    @Test
    fun `error state when api fails`() = runTest(testDispatcher) {
        fakeRepository.detailsErrorToThrow = RuntimeException("Ошибка API: 500")

        val viewModel = viewModelWithId(999)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.error)
        assertNull(state.movie)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `invalid movieId sets error without touching repository`() = runTest(testDispatcher) {
        val viewModel = viewModelWithId(0)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Некорректный ID фильма", state.error)
        assertNull(state.movie)
        assertEquals(0, fakeRepository.getMovieDetailsCallCount)
    }

    @Test
    fun `refresh reloads movie details`() = runTest(testDispatcher) {
        fakeRepository.movieDetailsToReturn = testMovie(id = 123, name = "Inception")

        val viewModel = viewModelWithId(123)
        advanceUntilIdle()

        viewModel.refresh()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.movie)
        assertEquals(2, fakeRepository.getMovieDetailsCallCount)
    }

    @Test
    fun `isFavorite reflects repository state`() = runTest(testDispatcher) {
        val movie = testMovie(id = 123, name = "Inception")
        fakeRepository.movieDetailsToReturn = movie

        val viewModel = viewModelWithId(123)
        advanceUntilIdle()

        assertEquals(false, viewModel.uiState.value.isFavorite)

        viewModel.toggleFavorite()
        advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.isFavorite)
    }

    @Test
    fun `toggleFavorite does nothing when no movie loaded`() = runTest(testDispatcher) {

        val viewModel = viewModelWithId(0)

        viewModel.toggleFavorite()
        advanceUntilIdle()

        assertEquals(false, viewModel.uiState.value.isFavorite)
    }
}
