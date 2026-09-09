package com.gerasimov_mobile_dev.movieflow.domain.model


data class Movie(
    val id: Int,
    val name: String?,
    val year: Int?,
    val description: String?,
    val shortDescription: String?,
    val movieLength: Int?,
    val isSeries: Boolean?,
    val ageRating: String?,
    val rating: Double?,
    val poster: String?,
    val backdrop: String?,
    val genres: String?,
    val countries: String?,
    val actors: String?,
    val trailers: List<Trailer>
)

data class Trailer(
    val url: String?,
    val name: String?,
    val site: String?,
    val type: String?
)