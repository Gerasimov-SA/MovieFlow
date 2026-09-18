package com.gerasimov_mobile_dev.movieflow.presentation.state

data class SearchFilters(
    val type: String? = null,       // "movie", "tv-series", "cartoon", "anime"
    val year: Int? = null,           // 2020
    val genre: String? = null,       // "драма"
    val minRating: Double? = null    // 7.0
) {
    fun hasActiveFilters(): Boolean =
        type != null || year != null || genre != null || minRating != null

}