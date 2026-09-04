package com.gerasimov_mobile_dev.movieflow.presentation.navigation

object Routes {
    const val MOVIES = "movies"
    const val MOVIE_DETAIL = "movie/{movieId}"

    fun movieDetail(movieId: Int) = "movie/$movieId"
}