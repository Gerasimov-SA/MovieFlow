package com.gerasimov_mobile_dev.movieflow.data.repository

import android.content.Context
import android.util.Log
import com.gerasimov_mobile_dev.movieflow.BuildConfig
import com.gerasimov_mobile_dev.movieflow.data.remote.MoviesResponseDto
import com.gerasimov_mobile_dev.movieflow.data.remote.PoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.data.remote.toDomain
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val api: PoiskKinoApi,
    private val context: Context,
) : MoviesRepository {
    override fun getMovies(): Flow<List<Movie>> = flow {
        val movies = readRemoteMovies()
        Log.d("DTO", movies.toString())
        emit(movies)
    }

    private suspend fun readRemoteMovies(): List<Movie> {
        val response = api.getMovies(
            apiKey = BuildConfig.PK_API_KEY,
        )
        if (response.isSuccessful) {
            val responseDto = response.body() ?: throw RuntimeException("Пустой ответ от API")
            Log.d("DTO", responseDto.toString())
            return responseDto.docs.map { it.toDomain() }
        } else {
            throw RuntimeException("Ошибка API: ${response.code()}")
        }
    }

}