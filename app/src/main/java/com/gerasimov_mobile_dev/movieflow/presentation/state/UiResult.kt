package com.gerasimov_mobile_dev.movieflow.presentation.state

sealed class UiResult<out T> {
    data class Success<out T>(val data: T) : UiResult<T>()
    data class Error(val exception: Throwable, val userMessage: String? = null) : UiResult<Nothing>()
    object Loading : UiResult<Nothing>()
}