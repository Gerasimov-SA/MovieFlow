package com.gerasimov_mobile_dev.movieflow

import com.gerasimov_mobile_dev.movieflow.data.remote.MovieDetailsDto
import com.gerasimov_mobile_dev.movieflow.data.remote.common.CountryDto
import com.gerasimov_mobile_dev.movieflow.data.remote.common.GenreDto
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepositoryImpl
import com.gerasimov_mobile_dev.movieflow.fake.FakePoiskKinoApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesRepositoryTest {

    private val fakeApi = FakePoiskKinoApi()
    private val repository = MoviesRepositoryImpl(fakeApi)

    @Test
    fun `getMoviesPagingSource returns non-null PagingSource`() {
        val pagingSource = repository.getMoviesPagingSource()
        assertNotNull(pagingSource)
    }

    @Test
    fun `getMovieDetails returns mapped domain model when API succeeds`() = runTest {
        val expectedId = 123
        val expectedName = "Inception"

        fakeApi.movieDetailsToReturn = mapOf(
            expectedId to MovieDetailsDto(
                id = expectedId,
                name = expectedName,
                year = 2010,
                rating = null,
                poster = null,
                backdrop = null,
                genres = listOf(GenreDto(name = "Sci-Fi"), GenreDto(name = "Action")),
                countries = listOf(CountryDto(name = "USA"))
            )
        )


        val movie = repository.getMovieDetails(expectedId)

        assertEquals(expectedId, movie.id)
        assertEquals(expectedName, movie.name)
        assertEquals(2010, movie.year)
        assertEquals("Sci-Fi, Action", movie.genres)
        assertEquals("USA", movie.countries)
    }

    @Test
    fun `getMovieDetails throws RuntimeException when API fails`() = runTest {
        fakeApi.shouldThrowError = true

        val exception = assertFailsWith<RuntimeException> {
            repository.getMovieDetails(123)
        }

        assertTrue(
            exception.message?.contains("Ошибка API") == true ||
                    exception.message?.contains("Network error") == true
        )
    }

    @Test
    fun `getMovieDetails throws when API returns null body`() = runTest {
        fakeApi.movieDetailsToReturn = emptyMap()

        val exception = assertFailsWith<RuntimeException> {
            repository.getMovieDetails(999)
        }
        assertTrue(exception.message?.contains("Пустой ответ от API") == true)
    }
}