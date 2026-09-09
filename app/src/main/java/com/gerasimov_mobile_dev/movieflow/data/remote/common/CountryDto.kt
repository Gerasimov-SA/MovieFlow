package com.gerasimov_mobile_dev.movieflow.data.remote.common

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryDto(
    val name: String? = null,
    val id: Int? = null
)