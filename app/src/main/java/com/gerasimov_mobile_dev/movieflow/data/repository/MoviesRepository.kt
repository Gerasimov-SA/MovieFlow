package com.gerasimov_mobile_dev.movieflow.data.repository

import androidx.paging.PagingSource
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
     suspend fun getMovieDetails(movieId:Int):Movie

    fun getMoviesPagingSource(): PagingSource<String, Movie>
    fun searchMoviesPagingSource(filters: SearchFilters): PagingSource<String, Movie>
    fun searchByNamePagingSource(query: String, limit: Int): PagingSource<Int, Movie>
}
