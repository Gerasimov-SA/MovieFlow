package com.gerasimov_mobile_dev.movieflow.domain.model


data class Movie(
    val id: Int,
    val name: String?,
    val year: Int?,
    val description: String?,
    val shortDescription: String?,
    val movieLength: Int?,
    val ageRating: String?,
    val rating: Double?,
    val poster: String?,
    val backdrop: String?,
    val genres: String?,
    val countries: String?,
)

