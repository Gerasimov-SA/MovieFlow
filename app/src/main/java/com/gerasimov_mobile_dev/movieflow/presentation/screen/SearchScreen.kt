package com.gerasimov_mobile_dev.movieflow.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExpandedDockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.gerasimov_mobile_dev.movieflow.presentation.SearchViewModel
import com.gerasimov_mobile_dev.movieflow.presentation.component.ActiveFiltersRow
import com.gerasimov_mobile_dev.movieflow.presentation.component.FiltersDialog
import com.gerasimov_mobile_dev.movieflow.presentation.component.MovieListContent
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.query


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit
) {
    val lazyPagingItems = viewModel.searchFlow.collectAsLazyPagingItems()
    val filters by viewModel.filters.collectAsStateWithLifecycle()
    val currentQuery by viewModel.query.collectAsStateWithLifecycle()

    val textFieldState = rememberTextFieldState( currentQuery)
    val searchBarState = rememberSearchBarState()
    val scope = rememberCoroutineScope()
    var showFiltersDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(textFieldState.text) {
        viewModel.setQuery(textFieldState.text.toString())
    }

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            searchBarState = searchBarState,
            textFieldState = textFieldState,
            onSearch = {

                scope.launch { searchBarState.animateToCollapsed() }
            }
            ,
            placeholder = { Text("Поиск по названию") },
            leadingIcon = {
                if (searchBarState.currentValue == SearchBarValue.Expanded) {
                    IconButton(
                        onClick = {
                            scope.launch { searchBarState.animateToCollapsed() }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack ,
                            contentDescription = "Свернуть"
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Поиск"
                    )
                }
            },
            trailingIcon = {
                BadgedBox(
                    badge = {
                        if (filters.hasActiveFilters()) {
                            Badge()
                        }
                    }
                ) {
                    IconButton(onClick = { showFiltersDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Фильтры"
                        )
                    }
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .semantics { isTraversalGroup = true }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp)
                .semantics { traversalIndex = 1f }
        ) {
            if (filters.hasActiveFilters()) {
                ActiveFiltersRow(
                    filters = filters,
                    onClear = { viewModel.clearAll() }
                )
                MovieListContent(
                    lazyPagingItems = lazyPagingItems,
                    onMovieClick = onMovieClick
                )
            } else if(currentQuery.isNotBlank())
            MovieListContent(
                lazyPagingItems = lazyPagingItems,
                onMovieClick = onMovieClick
            )

        }

        // --- Свёрнутая поисковая строка ---
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .semantics { traversalIndex = 0f },
            state = searchBarState,
            inputField = inputField,
        )

        // --- Раскрытая строка (всплывает поверх списка) ---
//        ExpandedDockedSearchBar(
//            state = searchBarState,
//            inputField = inputField,
//        ) {
//
//            Column(Modifier.verticalScroll(rememberScrollState())) {
//                ListItem(
//                    headlineContent = { Text("Начните вводить название фильма") },
//                    leadingContent = {
//                        Icon(
//                            imageVector = Icons.Default.Info,
//                            contentDescription = null
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
    }

    // --- Диалог фильтров ---
    if (showFiltersDialog) {
        FiltersDialog(
            currentFilters = filters,
            onDismiss = { showFiltersDialog = false },
            onApply = { newFilters ->
                viewModel.updateType(newFilters.type)
                viewModel.updateGenre(newFilters.genre)
                viewModel.updateYearRange(newFilters.yearFrom, newFilters.yearTo)
                viewModel.updateRatingRange(newFilters.minRating, newFilters.maxRating)
                showFiltersDialog = false
            }
        )
    }
}