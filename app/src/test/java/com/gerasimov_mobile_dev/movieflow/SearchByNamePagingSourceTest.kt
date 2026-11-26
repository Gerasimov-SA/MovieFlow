package com.gerasimov_mobile_dev.movieflow

import androidx.paging.PagingSource
import com.gerasimov_mobile_dev.movieflow.data.remote.SearchRequestDto
import com.gerasimov_mobile_dev.movieflow.data.repository.SearchByNamePagingSource
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.fake.FakePoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.fake.testSearchMovieDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchByNamePagingSourceTest {

    private val fakeApi = FakePoiskKinoApi()

    private fun createSource(query: String = "dune", limit: Int = 20) =
        SearchByNamePagingSource(
            fakeApi,
            SearchRequestDto(query = query, limit = limit)
        )

    private suspend fun loadFirstPage(
        source: PagingSource<Int, Movie>
    ): PagingSource.LoadResult.Page<Int, Movie> {
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
    fun `query reaches api`() = runTest {
        fakeApi.searchResultsToReturn = listOf(testSearchMovieDto(id = 1))
        fakeApi.totalPages = 1

        loadFirstPage(createSource(query = "dune"))

        assertEquals("dune", fakeApi.lastSearchQuery)
    }

    @Test
    fun `first page loads without key`() = runTest {
        fakeApi.searchResultsToReturn = listOf(testSearchMovieDto(id = 1))
        fakeApi.totalPages = 3

        val result = loadFirstPage(createSource())

        assertEquals(1, result.data.size)
        assertEquals(1, fakeApi.lastSearchPage)
    }

    @Test
    fun `next key computed from pages when more pages exist`() = runTest {
        fakeApi.searchResultsToReturn = listOf(testSearchMovieDto(id = 1))
        fakeApi.totalPages = 3

        val result = loadFirstPage(createSource())

        assertEquals(2, result.nextKey)
    }

    @Test
    fun `last page has null next key`() = runTest {
        fakeApi.searchResultsToReturn = listOf(testSearchMovieDto(id = 1))
        fakeApi.totalPages = 1

        val result = loadFirstPage(createSource())

        assertNull(result.nextKey)
    }

    @Test
    fun `pages null means single page - no next key`() = runTest {
        fakeApi.searchResultsToReturn = listOf(testSearchMovieDto(id = 1))
        fakeApi.totalPages = null

        val result = loadFirstPage(createSource())

        assertNull(result.nextKey)
    }

    @Test
    fun `append passes next page number`() = runTest {
        fakeApi.searchResultsToReturn = listOf(testSearchMovieDto(id = 5))
        fakeApi.totalPages = 3
        val source = createSource()

        source.load(
            PagingSource.LoadParams.Append(
                key = 2,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertEquals(2, fakeApi.lastSearchPage)
    }

    @Test
    fun `empty results produce empty page without next key`() = runTest {
        fakeApi.searchResultsToReturn = emptyList()
        fakeApi.totalPages = 1

        val result = loadFirstPage(createSource())

        assertEquals(0, result.data.size)
        assertNull(result.nextKey)
    }

    @Test
    fun `http error becomes LoadResult Error`() = runTest {
        fakeApi.searchErrorCode = 500

        val result = createSource().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
    }
}
