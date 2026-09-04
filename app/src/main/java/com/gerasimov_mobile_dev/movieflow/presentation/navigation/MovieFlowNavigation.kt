package com.gerasimov_mobile_dev.movieflow.presentation.navigation

import androidx.compose.runtime.Composable
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
        startDestination = Routes.MOVIES
    ) {
        composable(Routes.MOVIES) {
            MoviesListScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Routes.movieDetail(movieId))
                }
            )
        }
        composable(
            route = Routes.MOVIE_DETAIL,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MovieDetailsScreen(
                movieId = movieId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}