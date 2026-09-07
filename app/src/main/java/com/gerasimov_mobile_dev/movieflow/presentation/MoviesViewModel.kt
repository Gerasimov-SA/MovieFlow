package com.gerasimov_mobile_dev.movieflow.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepository
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepositoryImpl
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MoviesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val movies: List<Movie> = emptyList()
)

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository//Не MoviesRepositoryImpl потому что hilt сам находит эту реализацию(но ее нужно забиндить в AppModule)
) : ViewModel(){

    val uiState = repository.getMovies()   //берем поток Flow от репозитория в котором мы заэмители список фильмов
        .map { MoviesUiState(movies = it) }  //Преобразуем список в экземпляр MoviesUiState для того чтоб добавить состояния isLoading и error
        .stateIn(viewModelScope, SharingStarted.Lazily, MoviesUiState())//Преобразуем холодный поток Flow в горячий StateFlow, чтобы подписчики всегда получяли только последний свежий список фильмов
    //SharingStarted.Lazily - репозиторий начнёт читать JSON и отдавать данные только тогда, когда Compose-экран первый раз подпишется на uiState.
    //SharingStarted.Eagerly, поток стартует сразу при создании ViewModel, даже если экран ещё не открыт.
     //Для реальных API это важно: не делать запросы, пока экран не виден.
   //initialValue = MoviesUiState() Это начальное значение, которое «лежит в буфере» до того, как придут реальные данные.

    fun refresh() {
        // Для полноценной реализации тут нужно перезапустить поток.
        // Самый простой способ — сделать отдельный Flow для рефреша и объединить через merge.
        // Пока для заглушки можно просто оставить как есть или сделать простой перезапуск.
    }
}