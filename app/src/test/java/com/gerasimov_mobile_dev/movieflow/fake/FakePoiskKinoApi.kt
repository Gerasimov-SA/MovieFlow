package com.gerasimov_mobile_dev.movieflow.fake

import com.gerasimov_mobile_dev.movieflow.data.remote.MovieDetailsDto
import com.gerasimov_mobile_dev.movieflow.data.remote.MovieDto
import com.gerasimov_mobile_dev.movieflow.data.remote.MoviesResponseDto
import com.gerasimov_mobile_dev.movieflow.data.remote.PoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.data.remote.SearchByNameResponseDto
import com.gerasimov_mobile_dev.movieflow.data.remote.SearchMovieDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakePoiskKinoApi : PoiskKinoApi {


    var moviesToReturn: List<MovieDto> = emptyList()
    var movieDetailsToReturn: Map<Int, MovieDetailsDto> = emptyMap()
    var searchResultsToReturn: List<SearchMovieDto> = emptyList()


    var moviesErrorCode: Int? = null
    var movieDetailsErrorCode: Int? = null
    var searchErrorCode: Int? = null


    var nextToken: String? = null
    var totalPages: Int? = 1


    var getMoviesCallCount = 0
    var searchMoviesCallCount = 0
    var lastRequestedFilters: Map<String, List<String>?> = emptyMap()
        private set
    var lastRequestedNext: String? = null
        private set
    var lastSearchQuery: String? = null
        private set
    var lastSearchPage: Int? = null
        private set

    override suspend fun getMovies(
        apiKey: String,
        limit: Int,
        sortField: List<String>,
        sortType: List<String>,
        next: String?,
        notNullFields: List<String>,
        type: List<String>?,
        year: List<String>?,
        genresName: List<String>?,
        ratingKp: List<String>?
    ): Response<MoviesResponseDto> {
        getMoviesCallCount++
        moviesErrorCode?.let { return error(it) }
        lastRequestedNext = next
        lastRequestedFilters = mapOf(
            "type" to type,
            "year" to year,
            "genres.name" to genresName,
            "rating.kp" to ratingKp,
        )

        return Response.success(
            MoviesResponseDto(
                docs = moviesToReturn,
                limit = limit,
                next = nextToken,
                prev = null,
                hasNext = nextToken != null,
                hasPrev = false
            )
        )
    }

    override suspend fun getMovieDetails(id: Int, apiKey: String): Response<MovieDetailsDto> {
        movieDetailsErrorCode?.let { return error(it) }

        val dto = movieDetailsToReturn[id]

        return if (dto != null) Response.success(dto) else error(404)
    }

    override suspend fun searchMovies(
        apiKey: String,
        query: String,
        page: Int,
        limit: Int
    ): Response<SearchByNameResponseDto> {
        searchMoviesCallCount++
        searchErrorCode?.let { return error(it) }

        lastSearchQuery = query
        lastSearchPage = page
        return Response.success(
            SearchByNameResponseDto(
                docs = searchResultsToReturn,
                total = searchResultsToReturn.size,
                limit = limit,
                page = page,
                pages = totalPages
            )
        )
    }

    private fun <T> error(code: Int): Response<T> = Response.error(
        code,
        "API error: HTTP $code".toResponseBody("text/plain".toMediaTypeOrNull())
    )
}