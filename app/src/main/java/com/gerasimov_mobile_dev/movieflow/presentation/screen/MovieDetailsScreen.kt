package com.gerasimov_mobile_dev.movieflow.presentation.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.gerasimov_mobile_dev.movieflow.presentation.MoviesViewModel

@Composable
fun MovieDetailsScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    movieId: Int,
    onBackClick: () -> Boolean,
){
    BoxScreen("Детали фильма")}