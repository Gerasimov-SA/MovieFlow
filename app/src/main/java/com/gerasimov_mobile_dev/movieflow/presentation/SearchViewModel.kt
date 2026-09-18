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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update



@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MoviesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _filters = MutableStateFlow(
        savedStateHandle.get("search_filters") ?: SearchFilters()
    )
    val filters: StateFlow<SearchFilters> = _filters.asStateFlow()

    val searchFlow: Flow<PagingData<Movie>> = _filters
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { currentFilters ->
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = true,
                    initialLoadSize = 40
                ),
                pagingSourceFactory = { repository.searchMoviesPagingSource(currentFilters) }
            ).flow
        }
        .cachedIn(viewModelScope)




    fun applyFilters(filters: SearchFilters) {
        _filters.value = filters
        saveState()
    }

    fun clearAll() {
        _filters.value = SearchFilters()
        saveState()
    }
    private fun saveState() {

    }
    fun updateType(type: String?) {}
    fun updateYear(year: Int?) {}
    fun updateGenre(genre: String?) {}
    fun updateMinRating(minRating: Double?) {}
}
