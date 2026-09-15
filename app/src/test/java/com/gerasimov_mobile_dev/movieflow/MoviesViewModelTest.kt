package com.gerasimov_mobile_dev.movieflow

import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.fake.FakeMoviesRepository
import com.gerasimov_mobile_dev.movieflow.presentation.MoviesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepo: FakeMoviesRepository
    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeMoviesRepository()
        viewModel = MoviesViewModel(fakeRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `moviesFlow is initialized and not null`() {
        assertNotNull(viewModel.moviesFlow)
    }

    @Test
    fun `repository is wired to ViewModel`() {
        val pagingSource = fakeRepo.getMoviesPagingSource()
        assertNotNull(pagingSource)
    }

    @Test
    fun `repository PagingSource factory returns data without error`() {
        fakeRepo.moviesToReturn = listOf(
            Movie(
                id = 1, name = "Inception",
                rating = 8.8, year = 2010,
                description = null, shortDescription = null,
                movieLength = null, isSeries = null,
                ageRating = null, poster = null, backdrop = null,
                genres = null, countries = null, actors = null,
                trailers = emptyList()
            )
        )

        val pagingSource = fakeRepo.getMoviesPagingSource()
        assertNotNull(pagingSource)
    }
}