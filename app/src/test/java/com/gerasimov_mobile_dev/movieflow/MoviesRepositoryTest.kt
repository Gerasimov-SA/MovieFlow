package com.gerasimov_mobile_dev.movieflow

import com.gerasimov_mobile_dev.movieflow.data.local.FavoriteMovieEntity
import com.gerasimov_mobile_dev.movieflow.data.remote.MovieDetailsDto
import com.gerasimov_mobile_dev.movieflow.data.remote.common.GenreDto
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepositoryImpl
import com.gerasimov_mobile_dev.movieflow.fake.FakeFavoriteMovieDao
import com.gerasimov_mobile_dev.movieflow.fake.FakePoiskKinoApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class MoviesRepositoryTest {

    private val fakeApi = FakePoiskKinoApi()
    private val fakeDao = FakeFavoriteMovieDao()
    private val repository = MoviesRepositoryImpl(fakeApi, fakeDao)

    // Детали фильма

    @Test
    fun `getMovieDetails maps dto to domain`() = runTest {
        fakeApi.movieDetailsToReturn = mapOf(
            123 to MovieDetailsDto(
                id = 123,
                name = "Inception",
                year = 2010,
                rating = com.gerasimov_mobile_dev.movieflow.data.remote.common.RatingDto(kp = 8.8),
                genres = listOf(GenreDto(name = "Sci-Fi"), GenreDto(name = "Action")),
            )
        )

        val movie = repository.getMovieDetails(123)

        assertEquals(123, movie.id)
        assertEquals("Inception", movie.name)
        assertEquals(2010, movie.year)
        assertEquals(8.8, movie.rating, 0.001)
        assertEquals("Sci-Fi, Action", movie.genres)
    }

    @Test
    fun `getMovieDetails throws on http 404`() = runTest {


        val exception = try {
            repository.getMovieDetails(999)
            fail("Ожидалось исключение, а репозиторий вернул успех")
            null
        } catch (e: RuntimeException) {
            e
        }

        assertNotNull(exception)
        // HTTP-код 404
        assertTrue(exception!!.message!!.contains("404"))
    }

    @Test
    fun `getMovieDetails throws on null body`() = runTest {

        fakeApi.movieDetailsErrorCode = 500

        try {
            repository.getMovieDetails(999)
            fail("Ожидалось исключение")
        } catch (e: RuntimeException) {
            assertTrue(e.message!!.contains("500"))
        }
    }

    //Пейджинг

    @Test
    fun `getMoviesPagingSource returns non-null PagingSource`() {
        val pagingSource = repository.getMoviesPagingSource()
        assertNotNull(pagingSource)
    }

    @Test
    fun `filterMoviesPagingSource captures filters`() {
        val source = repository.filterMoviesPagingSource(
            com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters(minRating = 5.0)
        )
        assertNotNull(source)
    }

    //Избранное

    @Test
    fun `toggleFavorite adds then removes movie`() = runTest {
        val movie = com.gerasimov_mobile_dev.movieflow.fake.testMovie(id = 42, name = "Dune")

        repository.toggleFavorite(movie)

        assertTrue(fakeDao.observeIsFavorite(42).first())
        assertEquals(listOf(42), fakeDao.getFavoriteIds().first())

        repository.toggleFavorite(movie)

        assertEquals(false, fakeDao.observeIsFavorite(42).first())
        assertEquals(emptyList<Int>(), fakeDao.getFavoriteIds().first())
    }

    @Test
    fun `getFavorites maps entities to domain movies`() = runTest {
        fakeDao.seed(
            FavoriteMovieEntity(
                id = 5, name = "Arrival", year = 2016, rating = 8.1,
                poster = "url", genres = "Sci-Fi, Drama", addedAt = 1
            )
        )

        val movies = repository.getFavorites().first()

        assertEquals(1, movies.size)
        assertEquals("Arrival", movies[0].name)
        assertEquals(2016, movies[0].year)
        assertEquals(8.1, movies[0].rating, 0.001)
        assertEquals("Sci-Fi, Drama", movies[0].genres)
    }

    @Test
    fun `re-adding existing favorite replaces it with fresh addedAt`() = runTest {
        val movie = com.gerasimov_mobile_dev.movieflow.fake.testMovie(id = 42, name = "Dune")

        repository.toggleFavorite(movie)
        val firstAddedAt = fakeDao.getFavoriteIds().first() // добавлен

        repository.toggleFavorite(movie) // удалён
        repository.toggleFavorite(movie) // добавлен заново

        val favorites = fakeDao.getFavoriteIds().first()
        assertEquals(listOf(42), favorites)
    }
    @Test
    fun `getFavorites returns movies sorted by addedAt descending`() = runTest {
        fakeDao.seed(
            FavoriteMovieEntity(
                id = 1, name = "First", year = 2020, rating = 7.0,
                poster = "url", genres = "Drama", addedAt = 100
            ),
            FavoriteMovieEntity(
                id = 2, name = "Second", year = 2021, rating = 7.5,
                poster = "url", genres = "Action", addedAt = 200
            ),
            FavoriteMovieEntity(
                id = 3, name = "Third", year = 2022, rating = 8.0,
                poster = "url", genres = "Sci-Fi", addedAt = 300
            )
        )

        val movies = repository.getFavorites().first()

        assertEquals(listOf(3, 2, 1), movies.map { it.id })
    }
}
