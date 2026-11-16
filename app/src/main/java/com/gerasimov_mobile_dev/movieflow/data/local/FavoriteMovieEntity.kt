package com.gerasimov_mobile_dev.movieflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int,
    val name: String?,
    val year: Int?,
    val rating: Double,
    val poster: String?,
    val genres: String?,
    val addedAt: Long = System.currentTimeMillis()
)