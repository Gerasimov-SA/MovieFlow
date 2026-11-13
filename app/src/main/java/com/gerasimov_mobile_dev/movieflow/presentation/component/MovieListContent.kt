package com.gerasimov_mobile_dev.movieflow.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.presentation.screen.MovieItem

@Composable
fun MovieListContent(
    lazyPagingItems: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit
) {
    val refreshState = lazyPagingItems.loadState.refresh
    when (refreshState) {
        is LoadState.Loading -> {
            // Экран загрузки
            MoviesListSkeletonUi()
        }

        is LoadState.Error -> {

            ErrorScreen(
                message = refreshState.error.message ?: "Не удалось загрузить фильмы",
                onRetry = { lazyPagingItems.retry() }
            )
        }

        is LoadState.NotLoading -> {
            if (lazyPagingItems.itemCount == 0) {
                // Данные загружены, но список пуст
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Фильмов нет")
                }
            } else {
                // Основной список
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { it.id }
                    ) { index ->
                        val movie = lazyPagingItems[index]

                        if (movie == null) {
                            MovieItemSkeletonUi()
                        } else {
                            MovieItem(
                                movie = movie,
                                onClick = { onMovieClick(movie.id) }
                            )
                        }
                    }

                    // Индикатор подгрузки следующей страницы (хвост списка)
                    when (lazyPagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        is LoadState.Error -> {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    TextButton(onClick = { lazyPagingItems.retry() }) {
                                        Text("Ошибка загрузки. Повторить")
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}