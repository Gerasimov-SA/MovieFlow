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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.gerasimov_mobile_dev.movieflow.R
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
                    onBackClick = onBackClick,
                    isFavorite = state.isFavorite,
                    onFavoriteClick = viewModel::toggleFavorite,
                )
            }
        }
    }
}

@Composable
private fun MovieDetailsContent(
    movie: Movie,
    onBackClick: () -> Boolean,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.arrow_back_icon_description),
                    tint = Color.White
                )


            }
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) stringResource(R.string.remove_from_favorites_icon_description) else stringResource(
                        R.string.add_to_favorites_icon_description
                    ),
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.White
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
                        text = movie.name ?: stringResource(R.string.movie_name),
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
                        Text(stringResource(R.string.series), fontSize = 14.sp, color = Color.Gray)
                    }
                    // Рейтинг
                    if (movie.rating > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.rating_with_star, movie.rating),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFA000)
                            )
                        }
                    }

                    if (movie.movieLength != null) {
                        Text(
                            text = stringResource(R.string.movie_length_minutes, movie.movieLength),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    if (!movie.ageRating.isNullOrBlank()) {
                        Text(
                            text = stringResource(R.string.age_rating, movie.ageRating),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            if (!movie.genres.isNullOrBlank()) {
                InfoLine(title = stringResource(R.string.details_movie_genre), movie.genres)
            }


            if (!movie.countries.isNullOrBlank()) {
                InfoLine(
                    title = stringResource(R.string.details_movie_countries),
                    value = movie.countries
                )
            }


            val desc = movie.description ?: movie.shortDescription
            if (!desc.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.movie_description_label),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = desc, fontSize = 15.sp, lineHeight = 22.sp)
            }


            if (!movie.actors.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.movie_actors_label),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = movie.actors, fontSize = 15.sp, color = Color.Gray)
            }


            if (movie.trailers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.trailers_label),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                movie.trailers.forEach { trailer ->
                    Text(
                        text = "▶ ${trailer.name ?: stringResource(R.string.movie_trailer_default_name)}",
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

