package com.gerasimov_mobile_dev.movieflow.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gerasimov_mobile_dev.movieflow.R
import com.gerasimov_mobile_dev.movieflow.presentation.screen.FavoritesScreen
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

                    Destination.FAVORITES -> FavoritesScreen(
                        onMovieClick = { movieId ->
                            navController.navigate("movie/$movieId")
                        }
                    )
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
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    @StringRes val contentDescriptionRes: Int
) {
    MOVIES("movies", R.string.nav_movies, Icons.Default.Movie, R.string.nav_movies),
    SEARCH("search", R.string.nav_search, Icons.Default.Search, R.string.nav_search),
    FAVORITES("favorites", R.string.nav_favorites, Icons.Default.Favorite, R.string.nav_favorites)

}
