package com.gerasimov_mobile_dev.movieflow.data.remote

import com.squareup.moshi.JsonClass



@JsonClass(generateAdapter = true)
data class MovieDto(
    val id: Int,
    val name: String?,
    val alternativeName: String?,
    val enName: String?,
    val year: Int?,
    val description: String?,
    val shortDescription: String?,
    val movieLength: Int?,
    val ageRating: String?,
    val rating: RatingDto?,
    val poster: PosterDto?,
    val backdrop: BackdropDto?,
    val genres: List<GenreDto>?,
    val countries: List<CountryDto>?,
    val videos: VideosDto?
)
@JsonClass(generateAdapter = true)
data class MoviesResponseDto(
    val docs: List<MovieDto>,
    val limit: Int,
    val next: String?,
    val prev: String?,
    val hasNext: Boolean?,
    val hasPrev: Boolean?,
    val total: Int?
)
@JsonClass(generateAdapter = true)
data class RatingDto(
    val kp: Double?,
    val imdb: Double?,
    val tmdb: Double?,
    val filmCritics: Double?,
    val russianFilmCritics: Double?,
    val await: Double?
)

@JsonClass(generateAdapter = true)
data class PosterDto(
    val url: String?,
    val previewUrl: String?
)

@JsonClass(generateAdapter = true)
data class BackdropDto(
    val url: String?,
    val previewUrl: String?
)

@JsonClass(generateAdapter = true)
data class GenreDto(
    val name: String,
    val id: Int,
    val slug: String
)

@JsonClass(generateAdapter = true)
data class CountryDto(
    val name: String,
    val id: Int
)

@JsonClass(generateAdapter = true)
data class VideosDto(
    val trailers: List<TrailerDto>?
)

@JsonClass(generateAdapter = true)
data class TrailerDto(
    val url: String?,
    val name: String?,
    val site: String?,
    val type: String?
)