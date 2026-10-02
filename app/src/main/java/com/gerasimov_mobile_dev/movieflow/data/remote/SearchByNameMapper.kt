package com.gerasimov_mobile_dev.movieflow.data.remote

import com.gerasimov_mobile_dev.movieflow.data.remote.common.PosterDto
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import kotlin.collections.joinToString
import kotlin.collections.orEmpty
import kotlin.text.ifEmpty

fun SearchMovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        name = names?.firstOrNull()?.name ?: enName ?: alternativeName ?: "Без названия",
        rating = rating?.kp ?: 0.0,
        poster = poster?.previewUrl ?: poster?.url,
        year = releaseYears?.firstOrNull()?.start ?: releaseYears?.firstOrNull()?.end ,
        description = null,
        shortDescription = null,
        movieLength = null,
        isSeries = null,
        ageRating = null,
        backdrop = null,
        genres = genres.orEmpty().joinToString(", ") { it.name ?: "" }.ifEmpty { "—" },
        countries = null,
        actors = null,
        trailers = emptyList(),
    )
}
