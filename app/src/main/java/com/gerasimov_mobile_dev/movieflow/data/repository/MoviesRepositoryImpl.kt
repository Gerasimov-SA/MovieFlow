package com.gerasimov_mobile_dev.movieflow.data.repository


import androidx.paging.PagingSource
import com.gerasimov_mobile_dev.movieflow.BuildConfig
import com.gerasimov_mobile_dev.movieflow.data.local.FavoriteMovieDao
import com.gerasimov_mobile_dev.movieflow.data.local.FavoriteMovieEntity
import com.gerasimov_mobile_dev.movieflow.data.remote.PoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.data.remote.FiltersRequestDto
import com.gerasimov_mobile_dev.movieflow.data.remote.SearchRequestDto
import com.gerasimov_mobile_dev.movieflow.data.remote.toDomain
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val api: PoiskKinoApi,
    private val favoriteMovieDao: FavoriteMovieDao
) : MoviesRepository {

    override suspend fun getMovieDetails(movieId: Int): Movie {
        return withContext(Dispatchers.IO) {
            val response = api.getMovieDetails(
                id = movieId,
                apiKey = BuildConfig.PK_API_KEY
            )
            if (response.isSuccessful) {
                val responseDto = response.body() ?: throw RuntimeException("Пустой ответ от API")
                responseDto.toDomain()
            } else {
                throw RuntimeException("Ошибка API: ${response.code()}")
            }

        }
    }

    override fun getMoviesPagingSource(): PagingSource<String, Movie> {
        return MoviesPagingSource(api)
    }

    override fun filterMoviesPagingSource(filters: SearchFilters): PagingSource<String, Movie> {
        val request = FiltersRequestDto.fromFilters(filters)
        return FiltersMoviesPagingSource(api, request)
    }

    override fun searchByNamePagingSource(query: String, limit: Int): PagingSource<Int, Movie> {
        val request = SearchRequestDto(query, limit)
        return SearchByNamePagingSource(api, request)
    }

    override fun getFavorites(): Flow<List<Movie>> =
        favoriteMovieDao.getFavorites().map { entities -> entities.map { it.toDomain() } }

    override fun getFavoriteIds(): Flow<Set<Int>> =
        favoriteMovieDao.getFavoriteIds().map { it.toSet() }

    override fun observeIsFavorite(movieId: Int): Flow<Boolean> =
        favoriteMovieDao.observeIsFavorite(movieId)

    override suspend fun toggleFavorite(movie: Movie) = withContext(Dispatchers.IO) {
        val isFavorite = favoriteMovieDao.observeIsFavorite(movie.id).first()
        if (isFavorite) {
            favoriteMovieDao.deleteById(movie.id)
        } else {
            favoriteMovieDao.upsert(movie.toEntity())
        }
    }

    private fun FavoriteMovieEntity.toDomain() = Movie(
        id = id,
        name = name,
        year = year,
        description = null,
        shortDescription = null,
        movieLength = null,
        isSeries = null,
        ageRating = null,
        rating = rating,
        poster = poster,
        backdrop = null,
        genres = genres,
        countries = null,
        actors = null,
        trailers = emptyList()
    )

    private fun Movie.toEntity() = FavoriteMovieEntity(
        id = id,
        name = name,
        year = year,
        rating = rating,
        poster = poster,
        genres = genres,
    )

}