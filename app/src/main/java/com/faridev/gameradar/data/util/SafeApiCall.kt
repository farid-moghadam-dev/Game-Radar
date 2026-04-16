package com.faridev.gameradar.data.util

import com.faridev.gameradar.domain.model.AppError
import com.faridev.gameradar.presentation.common.state.UiState
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerializationException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Wraps a suspending network call and converts any thrown exception into a typed
 * [AppError] wrapped in [UiState.Error]. [CancellationException] is re-thrown so
 * coroutines can be cancelled cooperatively.
 */
@Suppress("TooGenericExceptionCaught")
suspend fun <T> safeApiCall(apiCall: suspend () -> T): UiState<T> =
    try {
        UiState.Success(apiCall())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        UiState.Error(e.toAppError())
    }

internal fun Throwable.toAppError(): AppError = when (this) {
    is ClientRequestException -> AppError.Http(response.status.value, response.status.description)
    is ServerResponseException -> AppError.Http(response.status.value, response.status.description)
    is RedirectResponseException -> AppError.Http(response.status.value, response.status.description)
    is ResponseException -> AppError.Http(response.status.value, response.status.description)
    is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException ->
        AppError.Timeout
    is IOException -> AppError.NoConnection
    is SerializationException -> AppError.Serialization(message)
    else -> AppError.Unknown(message)
}
