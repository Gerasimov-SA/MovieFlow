package com.gerasimov_mobile_dev.movieflow.data.remote

import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchRequestDto(
    val query: String?,
    val type: List<String>?,
    val year: List<String>?,
    val genresName: List<String>?,
    val ratingKp: List<String>?,
) {
    companion object {
        fun fromFilters(filters: SearchFilters): SearchRequestDto {
            return SearchRequestDto(
                query = filters.query.takeIf { it.isNotBlank() },
                type = filters.type?.let { listOf(it) },
                year = filters.year?.let { listOf(it.toString()) },
                genresName = filters.genre?.let { listOf(it) },
                ratingKp = filters.minRating?.let { listOf("${it}-10") },
            )
        }
    }
}