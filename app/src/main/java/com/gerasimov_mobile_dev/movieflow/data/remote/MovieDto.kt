package com.gerasimov_mobile_dev.movieflow.data.remote

import com.gerasimov_mobile_dev.movieflow.data.remote.common.CountryDto
import com.gerasimov_mobile_dev.movieflow.data.remote.common.GenreDto
import com.gerasimov_mobile_dev.movieflow.data.remote.common.RatingDto
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
//    val total: Int?
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



