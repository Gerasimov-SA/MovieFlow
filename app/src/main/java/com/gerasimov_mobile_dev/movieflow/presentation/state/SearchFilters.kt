package com.gerasimov_mobile_dev.movieflow.presentation.state

data class SearchFilters(
    val type: String? = null,
    val genre: String? = null,
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val minRating: Double? = null,
    val maxRating: Double? = null,
) {
    fun hasActiveFilters(): Boolean =
        type != null || genre != null || yearFrom != null || yearTo != null ||
                minRating != null || maxRating != null

    companion object {
        const val MIN_YEAR = 1900
        const val MOVIE_API_VALUE = "movie"
        const val SERIES_API_VALUE = "tv-series"
    }



}