package com.gerasimov_mobile_dev.movieflow.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gerasimov_mobile_dev.movieflow.R
import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters

@Composable
fun ActiveFiltersRow(
    filters: SearchFilters,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        filters.type?.let {
            ActiveFilterChip(if (it == SearchFilters.MOVIE_API_VALUE) stringResource(R.string.filters_movie_label) else stringResource(R.string.filters_series_label) )
        }
        filters.genre?.let {
            ActiveFilterChip(it)
        }

        var yearTo = filters.yearTo
        var yearFrom = filters.yearFrom
        if (yearTo != null && yearFrom != null && yearTo < yearFrom) {
            val temp = yearTo
            yearTo = yearFrom
            yearFrom = temp
        }
        val yearText = buildString {
            if (yearFrom != null) append(stringResource(R.string.active_filters_from, yearFrom))
            if (yearFrom != null && yearTo != null) append(" ")
            if (yearTo != null) append(stringResource(R.string.active_filters_to, yearTo))
        }
        if (yearText.isNotEmpty()) {
            ActiveFilterChip(stringResource(R.string.active_filters_year, yearText))
        }


        val ratingText = buildString {
            if (filters.minRating != null) append(
                stringResource(
                    R.string.active_filters_rating_from,
                    "%.1f".format(filters.minRating)
                ))
            if (filters.minRating != null && filters.maxRating != null) append(" ")
            if (filters.maxRating != null) append(
                stringResource(
                    R.string.active_filters_rating_to,
                    "%.1f".format(filters.maxRating)
                ))
        }
        if (ratingText.isNotEmpty()) {
            ActiveFilterChip(stringResource(R.string.active_filters_rating, ratingText))
        }

        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onClear) { Text(stringResource(R.string.active_filters_reset)) }
    }
}


@Composable
private fun ActiveFilterChip(label: String) {
    AssistChip(
        onClick = {},
        label = { Text(label, style = MaterialTheme.typography.labelMedium) }
    )
}