package com.gerasimov_mobile_dev.movieflow.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gerasimov_mobile_dev.movieflow.R
import com.gerasimov_mobile_dev.movieflow.domain.model.Genre
import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters
import java.util.Calendar


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
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    var yearFromState by remember {
        mutableIntStateOf(
            currentFilters.yearFrom ?: SearchFilters.MIN_YEAR
        )
    }
    var yearToState by remember { mutableIntStateOf(currentFilters.yearTo ?: currentYear) }

    // Диапазон рейтинга: 0.0 — 10.0
    var ratingStart by remember {
        mutableFloatStateOf(currentFilters.minRating?.toFloat() ?: 0f)
    }
    var ratingEnd by remember {
        mutableFloatStateOf(currentFilters.maxRating?.toFloat() ?: 10f)
    }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.filters_title)) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    stringResource(R.string.filters_type_label),
                    style = MaterialTheme.typography.titleSmall
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    FilterChip(
                        selected = type == SearchFilters.MOVIE_API_VALUE,
                        onClick = {
                            type =
                                if (type == SearchFilters.MOVIE_API_VALUE) null else SearchFilters.MOVIE_API_VALUE
                        },
                        label = { Text(stringResource(R.string.filters_movie_label)) }
                    )
                    FilterChip(
                        selected = type == SearchFilters.SERIES_API_VALUE,
                        onClick = {
                            type =
                                if (type == SearchFilters.SERIES_API_VALUE) null else SearchFilters.SERIES_API_VALUE
                        },
                        label = { Text(stringResource(R.string.filters_series_label)) }
                    )
                }

                // --- Жанр (выпадающий список) ---
                Text(
                    stringResource(R.string.movie_genre, ""),
                    style = MaterialTheme.typography.titleSmall
                )
                ExposedDropdownMenuBox(
                    expanded = genreMenuExpanded,
                    onExpandedChange = { genreMenuExpanded = it }
                ) {
                    val fillMaxWidth = Modifier
                        .fillMaxWidth()
                    OutlinedTextField(
                        value = genre
                            ?.let { Genre.fromApiValue(it) }
                            ?.let { stringResource(it.labelRes) }
                            ?: stringResource(R.string.filters_any),
                        onValueChange = {},
                        readOnly = true,
                        modifier = fillMaxWidth.menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            !genreMenuExpanded
                        ),
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
                            text = { Text(stringResource(R.string.filters_any)) },
                            onClick = {
                                genre = null
                                genreMenuExpanded = false
                            }
                        )
                        Genre.GENRES.forEach { g ->
                            DropdownMenuItem(
                                text = { Text(stringResource(g.labelRes)) },
                                onClick = {
                                    genre = g.apiValue
                                    genreMenuExpanded = false
                                }
                            )
                        }
                    }
                }


                Text(
                    stringResource(R.string.movie_year, ""),
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
                    },
                    currentYear = currentYear
                )


                // --- Рейтинг (RangeSlider) ---
                Text(
                    stringResource(
                        R.string.filters_rating_from_to,
                        "%.1f".format(ratingStart),
                        "%.1f".format(ratingEnd)
                    ),
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
                            yearFrom = yearFromState.takeIf { it != SearchFilters.MIN_YEAR },
                            yearTo = yearToState.takeIf { it != currentYear },
                            minRating = ratingStart.takeIf { it > 0f }?.toDouble(),
                            maxRating = ratingEnd.takeIf { it < 10f }?.toDouble()
                        )
                    )
                }
            ) { Text(stringResource(R.string.filters_apply_label)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.filters_cancel_label)) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearPickerRow(
    fromYear: Int?,
    toYear: Int?,
    onFromYearChange: (Int) -> Unit,
    onToYearChange: (Int) -> Unit,
    currentYear: Int
) {

    val yearsRange = (SearchFilters.MIN_YEAR..currentYear).toList()

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // От года
        var fromExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            modifier = Modifier.weight(1f),
            expanded = fromExpanded,
            onExpandedChange = { fromExpanded = it }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = !fromExpanded
                ),
                value = fromYear?.toString() ?: stringResource(R.string.filters_any),
                onValueChange = {},
                readOnly = true,
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded)
                },

                label = { Text(stringResource(R.string.filters_from_label)) }
            )
            DropdownMenu(
                expanded = fromExpanded,
                onDismissRequest = { fromExpanded = false },
                modifier = Modifier.height(240.dp)
            ) {

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
        ExposedDropdownMenuBox(
            modifier = Modifier.weight(1f),
            expanded = toExpanded,
            onExpandedChange = { toExpanded = it }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = !toExpanded
                ),
                value = toYear?.toString() ?: stringResource(R.string.filters_any),
                onValueChange = {},
                readOnly = true,

                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = toExpanded)
                },
                label = { Text(stringResource(R.string.filters_to_label)) }
            )
            DropdownMenu(
                expanded = toExpanded,
                onDismissRequest = { toExpanded = false },
                modifier = Modifier.height(240.dp)
            ) {

                yearsRange.sortedDescending().forEach { year ->

                    val enabled = fromYear == null || year > fromYear
                    if (enabled) {
                        DropdownMenuItem(
                            text = { Text(year.toString()) },
                            onClick = {
                                onToYearChange(year)
                                toExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}