package com.gerasimov_mobile_dev.movieflow.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepository
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    val movieId: Int = savedStateHandle.get("movieId") ?: 0

    private val _uiState =
        MutableStateFlow(DetailsUiState())//создаем изменяемый горячий поток в котором лежит состояние, делаем private чтобы менять можно было только внутри MovieDetailsViewModel, теперь никто из других слоев не сможет его менять
    val uiState: StateFlow<DetailsUiState> =
        _uiState.asStateFlow() //Здесь из потока ui_state делаем неизменяемый горячий поток. Сюда будем записывать уже измененное состояние и отдавать дрпугим слоям для чтения

    init {
        if (movieId <= 0) {
            _uiState.update { it.copy(error = "Некорректный ID фильма") }
        } else getDetails()

    }

    fun getDetails() {
        _uiState.update {
            it.copy(isLoading = true,error = null)
        }
        try {
            val data = repository.getMovieDetails(movieId)
            _uiState.update { it.copy(movie = data, isLoading = false) }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message ?: "Не удалось загрузить фильм") }
        }


    }

}
