package com.faridev.gameradar.data.util

import com.faridev.gameradar.presentation.common.state.UiState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): UiState<T> {
    return try {
        UiState.Success(apiCall())
    } catch (e: ClientRequestException) {
        UiState.Error("Client error: ${e.response.status}")
    } catch (e: ServerResponseException) {
        UiState.Error("Server error: ${e.response.status}")
    } catch (e: RedirectResponseException) {
        UiState.Error("Redirect error: ${e.response.status}")
    } catch (e: ResponseException) {
        UiState.Error("Response error: ${e.response.status}")
    } catch (_: HttpRequestTimeoutException) {
        UiState.Error("Request timeout")
    } catch (e: CancellationException) {
        throw e // Don't swallow coroutine cancellations
    } catch (e: Exception) {
        UiState.Error(e.localizedMessage ?: "Unknown error")
    }
}