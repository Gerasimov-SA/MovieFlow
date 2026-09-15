package com.gerasimov_mobile_dev.movieflow.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesPagingSource
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepository
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepositoryImpl
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds



@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    val moviesFlow: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = 20,              // Сколько грузить за раз (совпадает с limit по умолчанию в API)
            enablePlaceholders = true,  // КРИТИЧНО: создает null-элементы для твоих скелетонов
            initialLoadSize = 40        // При первом запуске загрузит 2 страницы сразу (40 фильмов)
        ),
        pagingSourceFactory = { repository.getMoviesPagingSource() }
    ).flow.cachedIn(viewModelScope)

    }


