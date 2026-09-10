package com.gerasimov_mobile_dev.movieflow.data.repository

import androidx.paging.PagingSource
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
     suspend fun getMovies(): List<Movie>
     suspend fun getMovieDetails(movieId:Int):Movie

    fun getMoviesPagingSource(): PagingSource<Int, Movie>
}
