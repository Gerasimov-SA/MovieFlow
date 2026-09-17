package com.gerasimov_mobile_dev.movieflow.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PoiskKinoApi {
    @GET("v1.5/movie")
    suspend fun getMovies(
        @Header("X-API-KEY") apiKey: String,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String = "rating.kp",
        @Query("order") order: String = "desc",
        @Query("next") next: String? = null,
        @Query("notNullFields") notNullFields: List<String> = listOf(
            "poster.url",
            "description"
        )

    ): Response<MoviesResponseDto>
    @GET("v1.5/movie/{id}")
        suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Header("X-API-KEY") apiKey: String
    ): Response<MovieDetailsDto>



}