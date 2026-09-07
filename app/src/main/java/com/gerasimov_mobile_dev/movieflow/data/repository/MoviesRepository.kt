package com.gerasimov_mobile_dev.movieflow.data.repository

import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
     fun getMovies(): Flow<List<Movie>>
     fun getMovieDetails(movieId:Int):Movie
}