package com.gerasimov_mobile_dev.movieflow.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gerasimov_mobile_dev.movieflow.BuildConfig
import com.gerasimov_mobile_dev.movieflow.data.remote.PoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.data.remote.toDomain
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchByNamePagingSource(
    private val api: PoiskKinoApi,
    private val query: String,
    private val limit: Int
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return withContext(Dispatchers.IO) {
            try {

                val page = params.key ?: 1

                val response = api.searchMovies(
                    apiKey = BuildConfig.PK_API_KEY,
                    query = query,
                    page = page,
                    limit = limit
                )

                if (!response.isSuccessful) {
                    return@withContext LoadResult.Error(Exception("Ошибка API: \${response.code()}"))
                }

                val body = response.body() ?: return@withContext LoadResult.Error(Exception("Пустой ответ"))

                val items = body.docs.map { it.toDomain() }


                val nextKey = if (page < body.pages) page + 1 else null

                LoadResult.Page(
                    data = items,
                    prevKey = null,
                    nextKey = nextKey
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}