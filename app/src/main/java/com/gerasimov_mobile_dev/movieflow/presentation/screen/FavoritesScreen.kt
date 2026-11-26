package com.gerasimov_mobile_dev.movieflow.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gerasimov_mobile_dev.movieflow.R
import com.gerasimov_mobile_dev.movieflow.presentation.FavoritesViewModel

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit

) {
    val favorites by viewModel.favorites.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    if (favorites.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = stringResource(R.string.favorites_empty_message),
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(favorites.size, key = { favorites[it].id }) { index ->
            MovieItem(
                movie = favorites[index],
                onClick = { onMovieClick(favorites[index].id) },
                isFavorite = favoriteIds.contains(favorites[index].id),
                onFavoriteClick = { movie ->
                    viewModel.toggleFavorite(movie)
                },
                modifier = Modifier.animateItem()
            )
        }
    }
}