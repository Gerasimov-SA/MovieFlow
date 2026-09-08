package com.gerasimov_mobile_dev.movieflow.data.repository


import android.util.Log
import com.gerasimov_mobile_dev.movieflow.BuildConfig
import com.gerasimov_mobile_dev.movieflow.data.remote.PoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.data.remote.toDomain
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val api: PoiskKinoApi,
) : MoviesRepository {
    override suspend fun getMovies(): List<Movie> {

        return withContext(Dispatchers.IO) {
            val response = api.getMovies(
                apiKey = BuildConfig.PK_API_KEY,
            )
            if (response.isSuccessful) {
                val responseDto = response.body() ?: throw RuntimeException("Пустой ответ от API")
                Log.d("DTO", responseDto.toString())
                responseDto.docs.map {
                    it.toDomain()
                }
            } else {
                throw RuntimeException("Ошибка API: ${response.code()}")
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Movie {
        return Movie(
            id = 666,
            name = "Человек паук",
            year = 2026,
            description = "Описание",
            shortDescription = "Короткое описанеи",
            movieLength = 2,
            ageRating ="",
            rating = 8.0,
            poster = null,
            backdrop = null,
            genres = null,
            countries = null
        )
    }

}