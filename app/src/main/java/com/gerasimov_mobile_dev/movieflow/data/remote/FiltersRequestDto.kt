package com.gerasimov_mobile_dev.movieflow.data.remote

import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FiltersRequestDto(
    val type: List<String>?,
    val year: List<String>?,
    val genresName: List<String>?,
    val ratingKp: List<String>?,
) {
    companion object {
        fun fromFilters(filters: SearchFilters): FiltersRequestDto {
            val yearString = buildString {
                if (filters.yearFrom != null) append(filters.yearFrom)
                if (filters.yearFrom != null && filters.yearTo != null) append("-")
                if (filters.yearTo != null && filters.yearTo != filters.yearFrom) append(filters.yearTo)
            }

            val ratingString = buildString {
                if (filters.minRating != null) append(filters.minRating)
                if (filters.minRating != null && filters.maxRating != null) append("-")
                if (filters.maxRating != null && filters.maxRating != filters.minRating) append(
                    filters.maxRating
                )
            }

            return FiltersRequestDto(
                type = filters.type?.let { listOf(it) },
                year = yearString.takeIf { it.isNotBlank() }?.let { listOf(it) },
                genresName = filters.genre?.let { listOf(it) },
                ratingKp = ratingString.takeIf { it.isNotBlank() }?.let { listOf(it) },
            )
        }
    }
}