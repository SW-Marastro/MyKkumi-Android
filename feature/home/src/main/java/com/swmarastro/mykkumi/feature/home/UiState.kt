package com.swmarastro.mykkumi.feature.home

sealed class UiState<out T : Any> {
    data class Success<out T : Any>(
        val data: T,
        val isFirstMeet: Boolean = false
    ) : UiState<T>()

    data class ApiError(
        val statusCode: String,
        val responseMessage: String,
    ) : UiState<Nothing>()

    data class Failure(
        val throwable: Throwable
    ) : UiState<Nothing>()
}