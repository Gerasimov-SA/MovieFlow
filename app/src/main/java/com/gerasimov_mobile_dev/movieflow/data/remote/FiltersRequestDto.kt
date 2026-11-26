package com.gerasimov_mobile_dev.movieflow.data.remote

import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters
import com.squareup.moshi.JsonClass
import java.util.Calendar
import java.util.Locale

@JsonClass(generateAdapter = true)
data class FiltersRequestDto(
    val type: List<String>?,
    val year: List<String>?,
    val genresName: List<String>?,
    val ratingKp: List<String>?,
) {
    companion object {
        fun fromFilters(filters: SearchFilters): FiltersRequestDto {
            val yearFrom = filters.yearFrom ?: SearchFilters.MIN_YEAR
            val yearTo = filters.yearTo ?: Calendar.getInstance().get(Calendar.YEAR)
            val year = if (filters.yearFrom != null || filters.yearTo != null) {
                listOf("$yearFrom-$yearTo")
            } else {
                null
            }
            val from = filters.minRating ?: 0.0
            val to = filters.maxRating ?: 10.0
            val ratingKp = if (filters.minRating != null || filters.maxRating != null) {
                listOf(String.format(Locale.US, "%.1f-%.1f", from, to))
            } else {
                null
            }

            return FiltersRequestDto(
                type = filters.type?.let { listOf(it) },
                year = year,
                genresName = filters.genre?.let { listOf(it) },
                ratingKp = ratingKp
            )
        }
    }
}