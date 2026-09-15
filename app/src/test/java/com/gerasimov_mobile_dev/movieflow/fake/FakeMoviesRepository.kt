package com.gerasimov_mobile_dev.movieflow.fake

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepository
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie

class FakeMoviesRepository : MoviesRepository {

    var moviesToReturn: List<Movie> = emptyList()
    var movieDetailsToReturn: Movie? = null
    var shouldThrowError = false
    var getMoviesPagingSourceCallCount = 0

    var getMovieDetailsCallCount = 0


    override suspend fun getMovieDetails(movieId: Int): Movie {
        getMovieDetailsCallCount++
        if (shouldThrowError) throw RuntimeException("Network error for details")
        return movieDetailsToReturn
            ?: Movie(
                id = movieId, name = "Test Movie $movieId",
                year = null,
                description = null,
                shortDescription = null,
                movieLength = null,
                isSeries = null,
                ageRating = null,
                rating = 8.0,
                poster = null,
                backdrop = null,
                genres = null,
                countries = null,
                actors = null,
                trailers = emptyList()
            )
    }

    override fun getMoviesPagingSource(): PagingSource<String, Movie> {
        getMoviesPagingSourceCallCount++
        return FakePagingSource(moviesToReturn, shouldThrowError)
    }
}

class FakePagingSource(
    private val movies: List<Movie>,
    private val shouldThrowError: Boolean
) : PagingSource<String, Movie>() {

    override fun getRefreshKey(state: PagingState<String, Movie>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Movie> {
        if (shouldThrowError) {
            return LoadResult.Error(RuntimeException("Network error"))
        }
        return LoadResult.Page(
            data = movies,
            prevKey = null,
            nextKey = null
        )
    }
}