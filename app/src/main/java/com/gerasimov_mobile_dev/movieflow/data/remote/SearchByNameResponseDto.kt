package com.gerasimov_mobile_dev.movieflow.data.remote

import com.gerasimov_mobile_dev.movieflow.data.remote.common.PosterDto
import com.squareup.moshi.JsonClass
import com.gerasimov_mobile_dev.movieflow.data.remote.common.GenreDto
import com.gerasimov_mobile_dev.movieflow.data.remote.common.RatingDto

@JsonClass(generateAdapter = true)
data class SearchByNameResponseDto(
    val docs: List<SearchMovieDto>,
    val total: Int?,
    val limit: Int?,
    val page: Int?,
    val pages: Int?
)

@JsonClass(generateAdapter = true)
data class SearchMovieDto(
    val id: Int,
    val names: List<NameDto>?,
    val poster: PosterDto?,
    val rating: RatingDto?,
    val genres: List<GenreDto>?,
    val releaseYears: List<ReleaseYearDto>?,
    val alternativeName: String?,
    val enName: String?,
    val year: Int?
)

@JsonClass(generateAdapter = true)
data class NameDto(val name: String?)


@JsonClass(generateAdapter = true)
data class ReleaseYearDto(val start: Int?, val end: Int?)