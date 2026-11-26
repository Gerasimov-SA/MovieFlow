package com.gerasimov_mobile_dev.movieflow.fake

import com.gerasimov_mobile_dev.movieflow.data.local.FavoriteMovieDao
import com.gerasimov_mobile_dev.movieflow.data.local.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeFavoriteMovieDao : FavoriteMovieDao {

    private val favorites = MutableStateFlow<List<FavoriteMovieEntity>>(emptyList())


    fun seed(vararg entities: FavoriteMovieEntity) {
        favorites.value = entities.toList()
    }


    override fun getFavorites(): Flow<List<FavoriteMovieEntity>> =
        favorites.map { list -> list.sortedByDescending { it.addedAt } }

    override fun getFavoriteIds(): Flow<List<Int>> =
        favorites.map { list -> list.map { it.id } }

    override fun observeIsFavorite(movieId: Int): Flow<Boolean> =
        favorites.map { list -> list.any { it.id == movieId } }

    override suspend fun upsert(movie: FavoriteMovieEntity) {
        favorites.value = favorites.value.filterNot { it.id == movie.id } + movie
    }

    override suspend fun deleteById(movieId: Int) {
        favorites.value = favorites.value.filterNot { it.id == movieId }
    }
}
