package com.gerasimov_mobile_dev.movieflow.data.remote.common

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatingDto(
    val kp: Double? = null,
    val imdb: Double? = null,
    val tmdb: Double? = null,
    val filmCritics: Double? = null,
    val russianFilmCritics: Double? = null,
    val await: Double? = null
)