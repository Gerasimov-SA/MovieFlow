package com.gerasimov_mobile_dev.movieflow

import androidx.lifecycle.SavedStateHandle
import com.gerasimov_mobile_dev.movieflow.data.remote.MovieDetailsDto
import com.gerasimov_mobile_dev.movieflow.data.remote.common.RatingDto
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepositoryImpl
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.fake.FakeMoviesRepository
import com.gerasimov_mobile_dev.movieflow.fake.FakePoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.presentation.MovieDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
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

    @Test
    fun `loading state is set immediately when ViewModel is created`() = runTest(testDispatcher) {
        val expectedMovie = Movie(
            id = 123,
            name = "Inception",
            year = 2010,
            description = null,
            shortDescription = null,
            movieLength = null,
            isSeries = false,
            ageRating = null,
            rating = 8.8,
            poster = null,
            backdrop = null,
            genres = null,
            countries = null,
            actors = null,
            trailers = emptyList()
        )

        val savedStateHandle = SavedStateHandle(mapOf("movieId" to 123))
        val viewModel = MovieDetailsViewModel(fakeRepository, savedStateHandle)


        val state = viewModel.uiState.value
        assertTrue(state.isLoading)
        assertNull(state.movie)
        assertNull(state.error)
    }

    @Test
    fun `movie details loaded successfully`() = runTest(testDispatcher) {
        // Подготавливаем данные прямо в репозитории
        val expectedMovie = Movie(
            id = 123,
            name = "Inception",
            year = 2010,
            description = null,
            shortDescription = null,
            movieLength = null,
            isSeries = false,
            ageRating = null,
            rating = 8.8,
            poster = null,
            backdrop = null,
            genres = null,
            countries = null,
            actors = null,
            trailers = emptyList()
        )
        fakeRepository.movieDetailsToReturn = expectedMovie

        val savedStateHandle = SavedStateHandle(mapOf("movieId" to 123))
        val viewModel = MovieDetailsViewModel(fakeRepository, savedStateHandle)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertNotNull(state.movie)
        assertEquals(123, state.movie?.id)
        assertEquals("Inception", state.movie?.name)
        assertEquals(8.8, state.movie?.rating)
    }

    @Test
    fun `error state when API throws exception`() = runTest(testDispatcher) {
        fakeRepository.shouldThrowError = true

        val savedStateHandle = SavedStateHandle(mapOf("movieId" to 999))
        val viewModel = MovieDetailsViewModel(fakeRepository, savedStateHandle)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertNull(state.movie)
        assertTrue(
            state.error!!.contains("Network error") ||
                    state.error!!.contains("error")
        )
    }

    @Test
    fun `error state when movieId is zero`() = runTest(testDispatcher) {
        val savedStateHandle = SavedStateHandle(mapOf("movieId" to 0))
        val viewModel = MovieDetailsViewModel(fakeRepository, savedStateHandle)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.error)
        assertEquals("Некорректный ID фильма", state.error)
        assertFalse(state.isLoading)
    }

    @Test
    fun `refresh reloads movie details`() = runTest(testDispatcher) {

        val firstMovie = Movie(
            id = 123,
            name = "Inception",
            year = 2010,
            rating = 8.8,
            description = null, shortDescription = null, movieLength = null,
            isSeries = false, ageRating = null, poster = null, backdrop = null,
            genres = null, countries = null, actors = null, trailers = emptyList()
        )
        fakeRepository.movieDetailsToReturn = firstMovie

        val savedStateHandle = SavedStateHandle(mapOf("movieId" to 123))
        val viewModel = MovieDetailsViewModel(fakeRepository, savedStateHandle)

        advanceUntilIdle()
        assertEquals("Inception", viewModel.uiState.value.movie?.name)

        val updatedMovie = Movie(
            id = 123,
            name = "Inception Updated",
            year = 2011,
            rating = 9.0,
            description = null, shortDescription = null, movieLength = null,
            isSeries = false, ageRating = null, poster = null, backdrop = null,
            genres = null, countries = null, actors = null, trailers = emptyList()
        )
        fakeRepository.movieDetailsToReturn = updatedMovie

        viewModel.refresh()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Inception Updated", state.movie?.name)
        assertEquals(9.0, state.movie?.rating)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }
}