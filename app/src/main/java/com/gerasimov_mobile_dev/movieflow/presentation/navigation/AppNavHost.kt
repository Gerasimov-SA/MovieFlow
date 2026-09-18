package com.gerasimov_mobile_dev.movieflow.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gerasimov_mobile_dev.movieflow.presentation.screen.MovieDetailsScreen
import com.gerasimov_mobile_dev.movieflow.presentation.screen.MoviesListScreen
import com.gerasimov_mobile_dev.movieflow.presentation.screen.SearchScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.MOVIES -> MoviesListScreen(
                        onMovieClick = { movieId ->
                            navController.navigate("movie/$movieId")
                        }
                    )
                    Destination.SEARCH -> SearchScreen(
                        onMovieClick = { movieId ->
                            navController.navigate("movie/$movieId")
                        }
                    )
                    Destination.FAVORITES -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Избранное скоро будет добавлено")
                        }
                    }
                }
            }
        }

        composable(
            route = "movie/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            MovieDetailsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    MOVIES("movies", "Главная", Icons.Default.Movie, "Фильмы"),
    SEARCH("search", "Поиск", Icons.Default.Search, "Поиск"),
    FAVORITES("favorites", "Избранное", Icons.Default.Favorite, "Избранное")
}
