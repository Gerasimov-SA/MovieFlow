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
import androidx.compose.ui.unit.dp
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
            AssistChip(
                onClick = {},
                label = { Text(if (it == "movie") "Фильм" else "Сериал") }
            )
        }
        filters.genre?.let {
            AssistChip(onClick = {}, label = { Text(it) })
        }


        val yearText = buildString {
            if (filters.yearFrom != null) append("от ${filters.yearFrom}")
            if (filters.yearFrom != null && filters.yearTo != null) append(" ")
            if (filters.yearTo != null) append("до ${filters.yearTo}")
        }
        if (yearText.isNotEmpty()) {
            AssistChip(onClick = {}, label = { Text("Год $yearText") })
        }


        val ratingText = buildString {
            if (filters.minRating != null) append("от ${"%.1f".format(filters.minRating)}")
            if (filters.minRating != null && filters.maxRating != null) append(" ")
            if (filters.maxRating != null) append("до ${"%.1f".format(filters.maxRating)}")
        }
        if (ratingText.isNotEmpty()) {
            AssistChip(onClick = {}, label = { Text("Рейтинг $ratingText") })
        }

        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onClear) { Text("Сбросить") }
    }
}


@Composable
private fun ActiveFilterChip(label: String) {
    AssistChip(
        onClick = {},
        label = { Text(label, style = MaterialTheme.typography.labelMedium) }
    )
}