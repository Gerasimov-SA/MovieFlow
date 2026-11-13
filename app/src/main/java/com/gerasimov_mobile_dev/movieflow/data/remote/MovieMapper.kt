package com.gerasimov_mobile_dev.movieflow.data.remote

import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.domain.model.Trailer

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        name = name ?: enName ?: alternativeName ?: "Без названия",
        year = year,
        description = description,
        shortDescription = shortDescription,
        movieLength = movieLength,
        ageRating = ageRating ?: "—",
        rating = rating?.kp ?: 0.0,
        poster = poster?.previewUrl ?: poster?.url,
        backdrop = backdrop?.url,
        genres = genres.orEmpty().joinToString(", ") { it.name ?: "" }.ifEmpty { "—" },
        countries = countries.orEmpty().joinToString(", ") { it.name ?: "" }.ifEmpty { "—" },
        isSeries = null,
        actors = null,
        trailers = emptyList()
    )

}
