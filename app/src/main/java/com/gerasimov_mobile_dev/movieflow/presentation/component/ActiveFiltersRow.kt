package com.gerasimov_mobile_dev.movieflow.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        filters.type?.let { ActiveFilterChip(label = it) }
        filters.year?.let { ActiveFilterChip(label = it.toString()) }
        filters.genre?.let { ActiveFilterChip(label = it) }
        filters.minRating?.let { ActiveFilterChip(label = "рейтинг от $it") }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onClear) {
            Text("Сбросить")
        }
    }
}

@Composable
private fun ActiveFilterChip(label: String) {
    AssistChip(
        onClick = {},
        label = { Text(label, style = MaterialTheme.typography.labelMedium) }
    )
}