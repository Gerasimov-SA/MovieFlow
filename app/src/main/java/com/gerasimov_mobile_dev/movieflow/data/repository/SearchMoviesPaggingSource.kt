package com.gerasimov_mobile_dev.movieflow.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gerasimov_mobile_dev.movieflow.BuildConfig
import com.gerasimov_mobile_dev.movieflow.data.remote.PoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.data.remote.SearchRequestDto
import com.gerasimov_mobile_dev.movieflow.data.remote.toDomain
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class SearchMoviesPagingSource @Inject constructor(
    private val api: PoiskKinoApi,
    private val request: SearchRequestDto
) : PagingSource<String, Movie>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Movie> {
        return withContext(Dispatchers.IO) {
            try {

                val cursor = params.key
                val response = api.getMovies(
                    apiKey = BuildConfig.PK_API_KEY,
                    type = request.type,
                    year = request.year,
                    genresName = request.genresName,
                    ratingKp = request.ratingKp,
                    notNullFields = listOf("poster.url", "name"),
                    limit = params.loadSize,
                    next = cursor,
                )

                if (!response.isSuccessful) {
                    return@withContext LoadResult.Error(Exception("Ошибка API при загрузке страницы: ${response.code()}"))
                }
                val body = response.body() ?: return@withContext LoadResult.Error(
                    Exception("Пустой ответ от API на странице \$page")
                )
                val items = body.docs.map { it.toDomain() }


                val nextKey = if (body.hasNext == true) {
                    body.next
                } else {
                    null
                }

                LoadResult.Page(
                    data = items,
                    prevKey = cursor,
                    nextKey = nextKey
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<String, Movie>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}