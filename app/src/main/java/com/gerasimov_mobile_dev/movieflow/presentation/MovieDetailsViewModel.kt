package com.gerasimov_mobile_dev.movieflow.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepository
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val movie: Movie? = null
)

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MoviesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> =
        _uiState.asStateFlow()
    val movieId: Int = savedStateHandle.get("movieId") ?: 0
    init {
        if (movieId > 0) {
            getDetails(movieId)
        } else {
            _uiState.update {
                it.copy(error = "Некорректный ID фильма", isLoading = false)
            }
        }
    }

    fun getDetails(movieId: Int) {
        _uiState.update {
            it.copy(isLoading = true, error = null)
        }
        viewModelScope.launch {
            try {
                val data = repository.getMovieDetails(movieId)
                _uiState.update { it.copy(movie = data, isLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Не удалось загрузить фильм", isLoading = false) }
            }
        }


    }
    fun refresh() {
        val movieId = savedStateHandle.get<Int>("movieId") ?: 0
        if (movieId > 0) getDetails(movieId)
    }

}
