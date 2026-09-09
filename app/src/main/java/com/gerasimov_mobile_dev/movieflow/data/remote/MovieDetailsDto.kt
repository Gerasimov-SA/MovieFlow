package com.gerasimov_mobile_dev.movieflow.data.remote

import com.gerasimov_mobile_dev.movieflow.data.remote.common.CountryDto
import com.gerasimov_mobile_dev.movieflow.data.remote.common.GenreDto
import com.gerasimov_mobile_dev.movieflow.data.remote.common.RatingDto
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class MovieDetailsDto(
    val id: Int? = null,
    val name: String? = null,
    val alternativeName: String? = null,
    val enName: String? = null,
    val description: String? = null,
    val shortDescription: String? = null,
    val year: Int? = null,
    val movieLength: Int? = null,
    val isSeries: Boolean? = null,
    val rating: RatingDto? = null,
    val ratingMpaa: String? = null,
    val ageRating: Int? = null,
    val poster: ShortImageDto? = null,
    val backdrop: ShortImageDto? = null,
    val genres: List<GenreDto>? = null,
    val countries: List<CountryDto>? = null,
    val slogan: String? = null,
    val type: String? = null,
    val persons: List<PersonDto>? = null,
    val videos: VideosDto? = null
)


@JsonClass(generateAdapter = true)
data class ShortImageDto(
    val url: String? = null,
    val previewUrl: String? = null
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


@JsonClass(generateAdapter = true)
data class PersonDto(
    val id: Int? = null,
    val name: String? = null,
    val enName: String? = null,
    val photo: String? = null,
    val profession: String? = null,
    val enProfession: String? = null
)
