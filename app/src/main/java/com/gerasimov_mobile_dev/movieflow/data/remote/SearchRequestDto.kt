package com.gerasimov_mobile_dev.movieflow.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchRequestDto(
    val query: String,
    val limit: Int
)