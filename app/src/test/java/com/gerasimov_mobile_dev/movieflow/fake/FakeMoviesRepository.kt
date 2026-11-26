package com.gerasimov_mobile_dev.movieflow.fake

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepository
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeMoviesRepository : MoviesRepository {



    var moviesToReturn: List<Movie> = emptyList()
    var searchResultsToReturn: List<Movie> = emptyList()


    var movieDetailsToReturn: Movie? = null
    var detailsErrorToThrow: Exception? = null



    var getMovieDetailsCallCount = 0
    var getMoviesPagingSourceCallCount = 0
    var filterMoviesPagingSourceCallCount = 0
    var lastAppliedFilters: SearchFilters? = null
        private set



    private val favoritesFlow = MutableStateFlow<List<Movie>>(emptyList())



    override suspend fun getMovieDetails(movieId: Int): Movie {
        getMovieDetailsCallCount++
        detailsErrorToThrow?.let { throw it }
        return movieDetailsToReturn
            ?: throw RuntimeException("Пустой ответ от API")
    }

    override fun getMoviesPagingSource(): PagingSource<String, Movie> {
        getMoviesPagingSourceCallCount++
        return FakeStringPagingSource(moviesToReturn)
    }

    override fun filterMoviesPagingSource(filters: SearchFilters): PagingSource<String, Movie> {
        filterMoviesPagingSourceCallCount++
        lastAppliedFilters = filters
        return FakeStringPagingSource(moviesToReturn)
    }

    override fun searchByNamePagingSource(query: String, limit: Int): PagingSource<Int, Movie> {
        return FakeIntPagingSource(searchResultsToReturn)
    }

    override fun getFavorites(): Flow<List<Movie>> = favoritesFlow

    override fun getFavoriteIds(): Flow<Set<Int>> =
        favoritesFlow.map { movies -> movies.map { it.id }.toSet() }

    override fun observeIsFavorite(movieId: Int): Flow<Boolean> =
        favoritesFlow.map { movies -> movies.any { it.id == movieId } }

    override suspend fun toggleFavorite(movie: Movie) {
        val current = favoritesFlow.value
        favoritesFlow.value = if (current.any { it.id == movie.id }) {
            current.filterNot { it.id == movie.id }
        } else {
            current + movie
        }
    }
}

class FakeStringPagingSource(
    private val movies: List<Movie>
) : PagingSource<String, Movie>() {

    override fun getRefreshKey(state: PagingState<String, Movie>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Movie> =
        LoadResult.Page(
            data = movies,
            prevKey = null,
            nextKey = null
        )
}

class FakeIntPagingSource(
    private val movies: List<Movie>
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
        LoadResult.Page(
            data = movies,
            prevKey = null,
            nextKey = null
        )
}