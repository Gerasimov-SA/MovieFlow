package com.gerasimov_mobile_dev.movieflow.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.presentation.MovieDetailsViewModel
import com.gerasimov_mobile_dev.movieflow.presentation.component.ErrorScreen
import com.gerasimov_mobile_dev.movieflow.presentation.component.MovieDetailsSkeletonContent
import com.gerasimov_mobile_dev.movieflow.presentation.component.MovieDetailsSkeletonHeader


@Composable
fun MovieDetailsScreen(
    onBackClick: () -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        state.isLoading && state.movie == null -> {

            Column(
                modifier = modifier.fillMaxSize()
            ) {
                MovieDetailsSkeletonHeader()
                MovieDetailsSkeletonContent()


            }
        }

        state.error != null && state.movie == null -> {
            ErrorScreen(
                message = state.error!!,
                onRetry = { viewModel.refresh() }
            )
        }

        else -> {
            val movie = state.movie
            if (movie != null) {
                MovieDetailsContent(
                    movie = movie,
                    onBackClick = onBackClick
                )
            }
        }
    }
}

@Composable
private fun MovieDetailsContent(
    movie: Movie,
    onBackClick: () -> Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            AsyncImage(
                model = movie.backdrop ?: movie.poster,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )



            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color.White
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = movie.poster,
                    contentDescription = movie.name,
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = movie.name ?: "Без названия",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    if (movie.year != null) {
                        Text(
                            text = movie.year.toString(),
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                    if (movie.isSeries == true) {
                        Text("Сериал", fontSize = 14.sp, color = Color.Gray)
                    }
                    // Рейтинг
                    if ( movie.rating > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "★ ${String.format("%.1f", movie.rating)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFA000)
                            )
                        }
                    }

                    if (movie.movieLength != null) {
                        Text(
                            text = "${movie.movieLength} мин",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    if (!movie.ageRating.isNullOrBlank()) {
                        Text(
                            text = "${movie.ageRating}+",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            if (!movie.genres.isNullOrBlank()) {
                InfoLine(title = "Жанры", value = movie.genres)
            }


            if (!movie.countries.isNullOrBlank()) {
                InfoLine(title = "Страны", value = movie.countries)
            }


            val desc = movie.description ?: movie.shortDescription
            if (!desc.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Описание",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = desc, fontSize = 15.sp, lineHeight = 22.sp)
            }


            if (!movie.actors.isNullOrBlank() && movie.actors != "—") {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "В ролях",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = movie.actors, fontSize = 15.sp, color = Color.Gray)
            }


            if (movie.trailers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Трейлеры",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                movie.trailers.forEach { trailer ->
                    Text(
                        text = "▶ ${trailer.name ?: "Трейлер"}",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InfoLine(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "$title: ",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Text(text = value, fontSize = 15.sp)
    }
}

