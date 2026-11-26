package com.gerasimov_mobile_dev.movieflow

import androidx.paging.testing.asSnapshot
import com.gerasimov_mobile_dev.movieflow.fake.FakeMoviesRepository
import com.gerasimov_mobile_dev.movieflow.fake.testMovie
import com.gerasimov_mobile_dev.movieflow.presentation.MoviesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
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
    fun `paging flow emits repository data`() = runTest(testDispatcher) {
        fakeRepo.moviesToReturn = listOf(
            testMovie(id = 1, name = "Inception"),
            testMovie(id = 2, name = "Interstellar")
        )

        val snapshot = viewModel.moviesFlow.asSnapshot()

        assertEquals(2, snapshot.size)
        assertEquals("Inception", snapshot[0].name)
        assertEquals("Interstellar", snapshot[1].name)
    }

    @Test
    fun `empty repository data produces empty list`() = runTest(testDispatcher) {

        val snapshot = viewModel.moviesFlow.asSnapshot()

        assertEquals(0, snapshot.size)
    }
}
