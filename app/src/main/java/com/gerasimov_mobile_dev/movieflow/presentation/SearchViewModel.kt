package com.gerasimov_mobile_dev.movieflow.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepository
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.milliseconds


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MoviesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _filters = MutableStateFlow(SearchFilters())
    val filters: StateFlow<SearchFilters> = _filters.asStateFlow()
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        // Восстанавливаем по полям
        _filters.value = SearchFilters(
            type = savedStateHandle.get("type"),
            genre = savedStateHandle.get("genre"),
            yearFrom = savedStateHandle.get("yearFrom"),
            yearTo = savedStateHandle.get("yearTo"),
            minRating = savedStateHandle.get("minRating"),
            maxRating = savedStateHandle.get("maxRating")
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchFlow: Flow<PagingData<Movie>> = combine(_query, _filters) { q, f ->
        if (q.isNotBlank()) {
            // Поиск по названию
            Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = true),
                pagingSourceFactory = { repository.searchByNamePagingSource(q, 20) }
            ).flow
        } else {
            // Поиск по фильтрам
            Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = true),
                pagingSourceFactory = { repository.filterMoviesPagingSource(f) }
            ).flow
        }
    }.debounce(300.milliseconds).distinctUntilChanged().flatMapLatest { it }
        .cachedIn(viewModelScope)

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun clearAll() {
        _filters.update { it.copy(
            type = null,
            genre = null,
            yearFrom = null,
            yearTo = null,
            minRating = null,
            maxRating = null
        ) }
        savedStateHandle["type"] = null
        savedStateHandle["genre"] = null
        savedStateHandle["yearFrom"] = null
        savedStateHandle["yearTo"] = null
        savedStateHandle["maxRating"] = null
        savedStateHandle["minRating"] = null
    }

    fun updateType(type: String?) {
        _filters.update { it.copy(type = type) }
        savedStateHandle["type"] = type
    }

    fun updateYearRange(from: Int?, to: Int?) {
        _filters.update {it.copy(yearFrom = from, yearTo = to) }
    }

    fun updateRatingRange(min: Double?, max: Double?) {
        _filters.update { it.copy(minRating = min, maxRating = max) }
    }

    fun updateGenre(genre: String?) {
        _filters.update { it.copy(genre = genre) }
        savedStateHandle["genre"] = genre
    }

}
