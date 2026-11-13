package com.gerasimov_mobile_dev.movieflow.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepository
import com.gerasimov_mobile_dev.movieflow.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    val moviesFlow: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = true,
            initialLoadSize = 40
        ),
        pagingSourceFactory = { repository.getMoviesPagingSource() }
    ).flow.cachedIn(viewModelScope)

    }


