package com.gerasimov_mobile_dev.movieflow.presentation.screen


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gerasimov_mobile_dev.movieflow.presentation.MovieDetailsViewModel
import com.gerasimov_mobile_dev.movieflow.presentation.component.ErrorScreen

@Composable
fun MovieDetailsScreen(
    onBackClick: () -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {

 val state by viewModel.uiState.collectAsStateWithLifecycle()
    when {
        state.isLoading && state.movie==null -> BoxScreen("Загрузка деталей")
        state.error != null -> ErrorScreen(state.error!!, {viewModel.refresh()})
        else -> {
            BoxScreen("Детали фильма еще не сделал")
        }

    }
}
