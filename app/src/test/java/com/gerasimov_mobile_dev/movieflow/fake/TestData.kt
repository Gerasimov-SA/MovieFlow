package com.gerasimov_mobile_dev.movieflow.fake

import com.gerasimov_mobile_dev.movieflow.data.remote.MovieDto
import com.gerasimov_mobile_dev.movieflow.data.remote.SearchMovieDto
import com.gerasimov_mobile_dev.movieflow.data.remote.common.GenreDto
import com.gerasimov_mobile_dev.movieflow.data.remote.common.RatingDto
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.domain.model.Trailer

fun testMovieDto(
    id: Int = 1,
    name: String? = "Inception",
    year: Int? = 2010,
    ratingKp: Double? = 8.8,
    genres: List<GenreDto>? = listOf(GenreDto("Sci-Fi"))
): MovieDto = MovieDto(
    id = id,
    name = name,
    alternativeName = null,
    enName = null,
    year = year,
    description = null,
    shortDescription = null,
    movieLength = null,
    ageRating = null,
    rating = RatingDto(kp = ratingKp),
    poster = null,
    backdrop = null,
    genres = genres,
    countries = null,
    videos = null
)

fun testSearchMovieDto(
    id: Int = 1,
    name: String? = "Inception",
    year: Int? = 2010,
    ratingKp: Double? = 8.8
): SearchMovieDto = SearchMovieDto(
    id = id,
    names = null,
    poster = null,
    rating = RatingDto(kp = ratingKp),
    genres = null,
    releaseYears = null,
    alternativeName = null,
    enName = name,
    year = year
)

fun testMovie(
    id: Int = 1,
    name: String? = "Inception",
    year: Int? = 2010,
    rating: Double = 8.8
): Movie = Movie(
    id = id,
    name = name,
    year = year,
    description = null,
    shortDescription = null,
    movieLength = null,
    isSeries = false,
    ageRating = null,
    rating = rating,
    poster = null,
    backdrop = null,
    genres = null,
    countries = null,
    actors = null,
    trailers = emptyList<Trailer>()
)
