package com.gerasimov_mobile_dev.movieflow.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepository
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    val favorites: StateFlow<List<Movie>> = repository.getFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//подписка остаётся ещё 5 секунд, на случай быстрого возврата пользователя на вкладку избранного
            initialValue = emptyList()
        )

    val favoriteIds: StateFlow<Set<Int>> = repository.getFavoriteIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            repository.toggleFavorite(movie)
        }
    }
}