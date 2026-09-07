package com.gerasimov_mobile_dev.movieflow.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gerasimov_mobile_dev.movieflow.presentation.screen.MovieDetailsScreen
import com.gerasimov_mobile_dev.movieflow.presentation.screen.MoviesListScreen

@Composable
fun MovieFlowNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "movies"
    ) {
        composable("movies") {
            MoviesListScreen(
                onMovieClick = { movieId ->
                    navController.navigate("movie/$movieId")
                }
            )
        }
        composable(
            route = "movie/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val handle = SavedStateHandle(mapOf("movieId" to movieId))

            MovieDetailsScreen(
                movieId = movieId,
                onBackClick = { navController.popBackStack()
                    },
            )
        }
    }
}