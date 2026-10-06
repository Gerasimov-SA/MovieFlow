package com.gerasimov_mobile_dev.movieflow.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters
import java.util.Calendar

private val GENRES = listOf(
    "драма", "комедия", "триллер", "боевик", "ужасы",
    "фантастика", "фэнтези", "мелодрама", "детектив", "аниме",
    "анимация", "биография", "история", "военный", "криминал",
    "приключения", "семейный", "документальный", "детский", "музыка"
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersDialog(
    currentFilters: SearchFilters,
    onDismiss: () -> Unit,
    onApply: (SearchFilters) -> Unit
) {
    var type by remember { mutableStateOf(currentFilters.type) }
    var genre by remember { mutableStateOf(currentFilters.genre) }
    var genreMenuExpanded by remember { mutableStateOf(false) }

    // Диапазон лет: по умолчанию от MIN_YEAR до MAX_YEAR
    var yearFromState by remember { mutableStateOf(currentFilters.yearFrom) }
    var yearToState by remember { mutableStateOf(currentFilters.yearTo) }

    // Диапазон рейтинга: 0.0 — 10.0
    var ratingStart by remember {
        mutableStateOf(currentFilters.minRating?.toFloat() ?: 0f)
    }
    var ratingEnd by remember {
        mutableStateOf(currentFilters.maxRating?.toFloat() ?: 10f)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Фильтры") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

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

                // --- Жанр (выпадающий список) ---
                Text("Жанр", style = MaterialTheme.typography.titleSmall)
                ExposedDropdownMenuBox(
                    expanded = genreMenuExpanded,
                    onExpandedChange = { genreMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = genre ?: "любой",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = genreMenuExpanded)
                        }
                    )
                    DropdownMenu(
                        modifier = Modifier.height(240.dp),
                        expanded = genreMenuExpanded,
                        onDismissRequest = { genreMenuExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("любой") },
                            onClick = {
                                genre = null
                                genreMenuExpanded = false
                            }
                        )
                        GENRES.forEach { g ->
                            DropdownMenuItem(
                                text = { Text(g) },
                                onClick = {
                                    genre = g
                                    genreMenuExpanded = false
                                }
                            )
                        }
                    }
                }


                Text(
                    "Год",
                    style = MaterialTheme.typography.titleSmall
                )

                YearPickerRow(
                    fromYear = yearFromState,
                    toYear = yearToState,
                    onFromYearChange = { yearFrom ->
                        yearFromState = yearFrom
                    },
                    onToYearChange = { yearTo ->
                        yearToState = yearTo
                    }
                )


                // --- Рейтинг (RangeSlider) ---
                Text(
                    "Рейтинг: ${"%.1f".format(ratingStart)} — ${"%.1f".format(ratingEnd)}",
                    style = MaterialTheme.typography.titleSmall
                )
                RangeSlider(
                    value = ratingStart..ratingEnd,
                    onValueChange = { range ->
                        ratingStart = range.start
                        ratingEnd = range.endInclusive
                    },
                    valueRange = 0f..10f,
                    steps = 9,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApply(
                        SearchFilters(
                            type = type,
                            genre = genre,
                            yearFrom = yearFromState,
                            yearTo = yearToState,
                            minRating =  ratingStart.toDouble(),
                            maxRating =  ratingEnd.toDouble()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearPickerRow(
    fromYear: Int?,
    toYear: Int?,
    onFromYearChange: (Int?) -> Unit,
    onToYearChange: (Int?) -> Unit
) {
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    val yearsRange = (1900..currentYear).toList()

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // От года
        var fromExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(modifier = Modifier.weight(1f),
            expanded = fromExpanded,
            onExpandedChange = { fromExpanded = it}
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = !fromExpanded
                ),
                value = fromYear?.toString() ?: "любой",
                onValueChange = {},
                readOnly = true,
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded)
                },

                label = { Text("От") }
            )
            DropdownMenu(
                expanded = fromExpanded,
                onDismissRequest = { fromExpanded = false },
                modifier = Modifier.height(240.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("любой") },
                    onClick = {
                        onFromYearChange(null)
                        fromExpanded = false
                    }
                )
                yearsRange.sortedDescending().forEach { year ->
                    DropdownMenuItem(
                        text = { Text(year.toString()) },
                        onClick = {
                            onFromYearChange(year)
                            fromExpanded = false
                        }
                    )
                }
            }
        }

        // До года
        var toExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(modifier = Modifier.weight(1f),
            expanded = toExpanded,
            onExpandedChange = { toExpanded = it }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = !fromExpanded
                ),
                value = toYear?.toString() ?: "любой",
                onValueChange = {},
                readOnly = true,

                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = toExpanded)
                },
                label = { Text("До") }
            )
            DropdownMenu(
                expanded = toExpanded,
                onDismissRequest = { toExpanded = false },
                modifier = Modifier.height(240.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("любой") },
                    onClick = {
                        onToYearChange(null)
                        toExpanded = false
                    }
                )
                yearsRange.sortedDescending().forEach { year ->

                    val enabled = toYear == null || year >= (fromYear ?: year)
                    if (enabled) {
                        DropdownMenuItem(
                            text = { Text(year.toString()) },
                            onClick = {
                                onToYearChange(year)
                                toExpanded = false
                            },
                            enabled = enabled
                        )
                    }
                }
            }
        }
    }
}