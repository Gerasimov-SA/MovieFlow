package com.gerasimov_mobile_dev.movieflow.presentation.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoviesListSkeletonUi(
    modifier: Modifier = Modifier,
    itemCount: Int = 6
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(itemCount) { _ ->
            MovieItemSkeletonUi(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun MovieItemSkeletonUi(
    modifier: Modifier = Modifier
) {
    AnimatedSkeletonBox(
        modifier = modifier,
        cornerRadius = 12.dp
    )
}


