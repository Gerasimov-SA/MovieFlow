package com.gerasimov_mobile_dev.movieflow.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SearchBar

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.gerasimov_mobile_dev.movieflow.presentation.SearchViewModel
import com.gerasimov_mobile_dev.movieflow.presentation.component.MovieListContent

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit
) {
    val lazyPagingItems = viewModel.searchFlow.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Строка поиска
        SearchBar(
            query = viewModel.query.value,
            onQueryChange = { viewModel.updateQuery(it) },
            onSearch = { /* можно сразу искать при вводе или по кнопке */ },
            modifier = Modifier.weight(1f)
        )

        // Кнопка фильтров (открывает отдельный экран или BottomSheet)
        OutlinedButton(
            onClick = { /* открыть экран фильтров или BottomSheet */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Фильтры")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Список результатов
        MovieListContent(lazyPagingItems, onMovieClick)
    }
}
}