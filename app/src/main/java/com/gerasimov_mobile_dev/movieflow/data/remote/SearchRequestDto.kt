package com.gerasimov_mobile_dev.movieflow.data.remote

data class SearchRequestDto(
    val query: String,
    val page: Int,
    val limit: Int
)