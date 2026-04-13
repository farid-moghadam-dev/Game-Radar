package com.faridev.gameradar.presentation.common.state

import com.faridev.gameradar.domain.model.AppError

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val error: AppError) : UiState<Nothing>() {
        val message: String get() = error.userMessage
    }
}
