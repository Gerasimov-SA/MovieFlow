package com.gerasimov_mobile_dev.movieflow.fake

import com.gerasimov_mobile_dev.movieflow.data.remote.MovieDetailsDto
import com.gerasimov_mobile_dev.movieflow.data.remote.MovieDto
import com.gerasimov_mobile_dev.movieflow.data.remote.MoviesResponseDto
import com.gerasimov_mobile_dev.movieflow.data.remote.PoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.data.remote.common.RatingDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakePoiskKinoApi : PoiskKinoApi {

    var moviesToReturn: List<MovieDto> = emptyList()
    var movieDetailsToReturn: Map<Int, MovieDetailsDto> = emptyMap()

    var shouldThrowError = false
    var nextToken: String? = null
    var hasNext: Boolean = true
    var hasPrev: Boolean = false

    override suspend fun getMovies(
        apiKey: String,
        limit: Int,
        sort: String,
        order: String,
        next: String?,
        notNullFields: List<String>
    ): Response<MoviesResponseDto> {
        if (shouldThrowError) {
            throw RuntimeException("Network error")
        }

        return Response.success(
            MoviesResponseDto(
                docs = moviesToReturn,
                next = nextToken,
                prev = null,
                limit = limit,
                hasNext = hasNext,
                hasPrev = hasPrev,
            )
        )
    }

    override suspend fun getMovieDetails(id: Int, apiKey: String): Response<MovieDetailsDto> {
        if (shouldThrowError) {

            val errorBody = "API error: server unavailable".toResponseBody(
                "text/plain".toMediaTypeOrNull()
            )
            return Response.error(500, errorBody)
        }

        val dto = movieDetailsToReturn[id]
        return if (dto != null) {
            Response.success(dto)
        } else {
            Response.success(null)
        }
    }
}
