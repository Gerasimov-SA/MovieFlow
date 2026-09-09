package com.gerasimov_mobile_dev.movieflow.data.remote.common

import com.gerasimov_mobile_dev.movieflow.data.remote.MovieDetailsDto
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.domain.model.Trailer

private fun <T> List<T>?.joinNames(transform: (T) -> String?): String =
    this?.filterNotNull()
        .orEmpty()
        .joinToString(", ") { transform(it) ?: "" }
        .ifEmpty { "—" }

fun MovieDetailsDto.toDomain(): Movie {
    val actorsList = persons
        ?.filter { it.enProfession == "actor" || it.profession == "актеры" }
        .orEmpty()

    return Movie(
        id = id ?: 0,
        name = name ?: alternativeName ?: enName ?: "Без названия",
        year = year,
        description = description,
        shortDescription = shortDescription,
        movieLength = movieLength,
        isSeries = isSeries,
        ageRating = ageRating?.toString(),
        rating = rating?.kp,
        poster = poster?.url,
        backdrop = backdrop?.url,
        genres = genres.joinNames { it.name },
        countries = countries.joinNames { it.name },
        actors = actorsList.joinNames { it.name ?: it.enName },
        trailers = videos?.trailers
            .orEmpty()
            .map { Trailer(it.url, it.name, it.site, it.type) }
            .ifEmpty { emptyList() }
    )
}