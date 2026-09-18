package com.gerasimov_mobile_dev.movieflow.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gerasimov_mobile_dev.movieflow.R


enum class BottomNavRoute(
    val route: String,
    val labelResId: Int,
    val icon: ImageVector
) {
    Movies("movies", R.string.nav_movies, Icons.Default.Movie),
    Search("search", R.string.nav_search, Icons.Default.Search),
    Favorites("favorites", R.string.nav_favorites, Icons.Default.Favorite),
}

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 3.dp
    ) {
        BottomNavRoute.entries.forEach { route ->
            val isSelected = currentRoute == route.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(route.route) },
                icon = {
                    Icon(
                        imageVector = route.icon,
                        contentDescription = stringResource(route.labelResId)
                    )
                },
                label = { Text(stringResource(route.labelResId)) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}