package com.gerasimov_mobile_dev.movieflow.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gerasimov_mobile_dev.movieflow.BuildConfig
import com.gerasimov_mobile_dev.movieflow.data.remote.PoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.data.remote.toDomain
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MoviesPagingSource @Inject constructor(
    private val api: PoiskKinoApi
) : PagingSource<String, Movie>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Movie> {
        return withContext(Dispatchers.IO) {
            try {

                val cursor = params.key
                val limit = params.loadSize
                val response = api.getMovies(
                    apiKey = BuildConfig.PK_API_KEY,
                    limit = limit,
                    next = cursor
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

