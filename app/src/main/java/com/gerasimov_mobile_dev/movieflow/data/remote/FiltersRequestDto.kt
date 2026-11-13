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
            var yearTo = filters.yearTo
            var yearFrom = filters.yearFrom
            if (yearTo != null && yearFrom != null && yearTo < yearFrom) {
                val temp = yearTo
                yearTo = yearFrom
                yearFrom = temp
            }
            val yearString = buildString {
                if (yearFrom != null) append(yearFrom)
                if (yearFrom != null && yearTo != null) append("-")
                if (yearTo != null && yearTo != yearFrom) append(yearTo)
            }

            val ratingString = buildString {
                if (filters.minRating != null) append("%.1f".format(filters.minRating))
                if (filters.minRating != null && filters.maxRating != null) append("-")
                if (filters.maxRating != null && filters.maxRating != filters.minRating) append("%.1f".format(filters.maxRating))
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