package com.gerasimov_mobile_dev.movieflow.presentation.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.presentation.MoviesViewModel
import com.gerasimov_mobile_dev.movieflow.presentation.component.AnimatedSkeletonBox
import com.gerasimov_mobile_dev.movieflow.presentation.component.ErrorScreen
import com.gerasimov_mobile_dev.movieflow.presentation.component.MovieItemSkeletonUi
import com.gerasimov_mobile_dev.movieflow.presentation.component.MoviesListSkeletonUi

@Composable
fun MoviesListScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit
) {
    val lazyPagingItems = viewModel.moviesFlow.collectAsLazyPagingItems()


    val refreshState = lazyPagingItems.loadState.refresh
    when (refreshState) {
        is LoadState.Loading -> {

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

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Фильмов нет")
                }
            } else {

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

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                SubcomposeAsyncImage(
                    model = movie.poster,
                    contentDescription = movie.name,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        AnimatedSkeletonBox(
                            modifier = Modifier.fillMaxSize(),
                            cornerRadius = 8.dp
                        )
                    },
                    error = {
                        AnimatedSkeletonBox(
                            modifier = Modifier.fillMaxSize(),
                            cornerRadius = 8.dp
                        )
                    }

                )
                val (color, textColor) = getRatingColors(movie.rating)
                if (movie.rating > 0.0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-8).dp, y = 8.dp)
                            .background(color, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "%.1f".format(movie.rating),
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = movie.name ?: "Без названия",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Дата выхода: ${movie.year}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "Жанр: ${movie.genres}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}


@Composable
private fun getRatingColors(rating: Double): Pair<Color, Color> {
    return when {
        rating >= 8.0 -> Color(0xFF2E7D32) to Color.White
        rating >= 7.0 -> Color(0xFFFF8A22) to Color.White
        rating >= 5.0 -> Color(0xFF6C6C6C) to Color.White
        else -> Color(0xFFF44336) to Color.White
    }
}




