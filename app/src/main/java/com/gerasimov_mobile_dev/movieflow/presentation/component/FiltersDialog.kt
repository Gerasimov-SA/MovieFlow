package com.gerasimov_mobile_dev.movieflow.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters

@Composable
fun FiltersDialog(
    currentFilters: SearchFilters,
    onDismiss: () -> Unit,
    onApply: (SearchFilters) -> Unit
) {
    var type by remember { mutableStateOf(currentFilters.type) }
    var year by remember { mutableStateOf(currentFilters.year) }
    var genre by remember { mutableStateOf(currentFilters.genre) }
    var minRating by remember { mutableStateOf(currentFilters.minRating) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Фильтры") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // Тип
                Text("Тип", style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = type == "movie",
                        onClick = { type = if (type == "movie") null else "movie" },
                        label = { Text("Фильм") }
                    )
                    FilterChip(
                        selected = type == "tv-series",
                        onClick = { type = if (type == "tv-series") null else "tv-series" },
                        label = { Text("Сериал") }
                    )
                }

                // Жанр
                Text("Жанр", style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    value = genre ?: "",
                    onValueChange = { genre = it.ifBlank { null } },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("например, драма") },
                    singleLine = true
                )

                // Год
                Text("Год", style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    value = year?.toString() ?: "",
                    onValueChange = { year = it.toIntOrNull() },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("например, 2020") },
                    singleLine = true
                )

                // Рейтинг
                Text("Минимальный рейтинг", style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    value = minRating?.toString() ?: "",
                    onValueChange = { minRating = it.toDoubleOrNull() },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("например, 7.0") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApply(
                        SearchFilters(
                            type = type,
                            year = year,
                            genre = genre,
                            minRating = minRating
                        )
                    )
                }
            ) { Text("Применить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}