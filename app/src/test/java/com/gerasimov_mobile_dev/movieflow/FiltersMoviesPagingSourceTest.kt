package com.gerasimov_mobile_dev.movieflow

import androidx.paging.PagingSource
import com.gerasimov_mobile_dev.movieflow.data.remote.FiltersRequestDto
import com.gerasimov_mobile_dev.movieflow.data.repository.FiltersMoviesPagingSource
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.fake.FakePoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.fake.testMovieDto
import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FiltersMoviesPagingSourceTest {

    private val fakeApi = FakePoiskKinoApi()

    private suspend fun loadFirstPage(
        source: PagingSource<String, Movie>
    ): PagingSource.LoadResult.Page<String, Movie> {
        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        return result as PagingSource.LoadResult.Page
    }

    @Test
    fun `rating 5-10 reaches api as full range`() = runTest {
        fakeApi.moviesToReturn = listOf(testMovieDto(id = 1))
        val source = FiltersMoviesPagingSource(
            fakeApi,
            FiltersRequestDto.fromFilters(SearchFilters(minRating = 5.0, maxRating = null))
        )

        loadFirstPage(source)

        assertEquals(listOf("5.0-10.0"), fakeApi.lastRequestedFilters["rating.kp"])
    }

    @Test
    fun `default filters send no rating parameter`() = runTest {
        fakeApi.moviesToReturn = listOf(testMovieDto(id = 1))
        val source = FiltersMoviesPagingSource(
            fakeApi,
            FiltersRequestDto.fromFilters(SearchFilters())
        )

        loadFirstPage(source)

        assertNull(fakeApi.lastRequestedFilters["rating.kp"])
    }

    @Test
    fun `genre and year reach api`() = runTest {
        fakeApi.moviesToReturn = listOf(testMovieDto(id = 1))
        val source = FiltersMoviesPagingSource(
            fakeApi,
            FiltersRequestDto.fromFilters(
                SearchFilters(genre = "драма", yearFrom = 2020, yearTo = 2026)
            )
        )

        loadFirstPage(source)

        assertEquals(listOf("драма"), fakeApi.lastRequestedFilters["genres.name"])
        assertEquals(listOf("2020-2026"), fakeApi.lastRequestedFilters["year"])
    }

    @Test
    fun `successful page returns movies and next key when hasNext`() = runTest {
        fakeApi.moviesToReturn = listOf(testMovieDto(id = 1), testMovieDto(id = 2))
        fakeApi.nextToken = "cursor-abc"
        val source = FiltersMoviesPagingSource(
            fakeApi,
            FiltersRequestDto.fromFilters(SearchFilters())
        )

        val result = loadFirstPage(source)

        assertEquals(2, result.data.size)
        assertEquals("cursor-abc", result.nextKey)
    }

    @Test
    fun `last page has null next key when hasNext is false`() = runTest {
        fakeApi.moviesToReturn = listOf(testMovieDto(id = 1))
        fakeApi.nextToken = null
        val source = FiltersMoviesPagingSource(
            fakeApi,
            FiltersRequestDto.fromFilters(SearchFilters())
        )

        val result = loadFirstPage(source)

        assertNull(result.nextKey)
    }

    @Test
    fun `http error becomes LoadResult Error`() = runTest {
        fakeApi.moviesErrorCode = 500
        val source = FiltersMoviesPagingSource(
            fakeApi,
            FiltersRequestDto.fromFilters(SearchFilters())
        )

        val result = source.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
    }

    @Test
    fun `append page passes cursor from previous load`() = runTest {
        fakeApi.moviesToReturn = listOf(testMovieDto(id = 3))
        fakeApi.nextToken = "cursor-2"
        val source = FiltersMoviesPagingSource(
            fakeApi,
            FiltersRequestDto.fromFilters(SearchFilters())
        )

        source.load(
            PagingSource.LoadParams.Append(
                key = "cursor-2",
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertEquals("cursor-2", fakeApi.lastRequestedNext)
    }

    @Test
    fun `refresh on first page sends null cursor`() = runTest {
        fakeApi.moviesToReturn = listOf(testMovieDto(id = 1))
        val source = FiltersMoviesPagingSource(
            fakeApi,
            FiltersRequestDto.fromFilters(SearchFilters())
        )

        loadFirstPage(source)

        assertNull(fakeApi.lastRequestedNext)
    }
}
