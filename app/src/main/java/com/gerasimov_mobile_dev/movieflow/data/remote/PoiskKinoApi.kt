package com.gerasimov_mobile_dev.movieflow.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PoiskKinoApi {
    @GET("v1.5/movie")
    suspend fun getMovies(
        @Header("X-API-KEY") apiKey: String,
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1,
        @Query("sort") sort: String = "rating.kp",
        @Query("order") order: String = "desc"
    ): Response<MoviesResponseDto>
}