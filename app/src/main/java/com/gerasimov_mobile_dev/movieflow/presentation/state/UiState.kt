package com.gerasimov_mobile_dev.movieflow.presentation.state

import android.graphics.Movie

sealed class UiState {
    object Loading : UiState()
    data class Success(val movies: List<Movie>) : UiState()
    data class Error(val message: String) : UiState()
}