package com.faridev.gameradar.data.util

import com.faridev.gameradar.domain.model.AppError
import com.faridev.gameradar.presentation.common.state.UiState
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class SafeApiCallTest {

    @Test
    fun `returns Success for a successful call`() = runBlocking {
        val result = safeApiCall { "ok" }
        assertEquals(UiState.Success("ok"), result)
    }

    @Test
    fun `maps IOException to NoConnection`() = runBlocking {
        val result = safeApiCall<String> { throw IOException("down") }
        assertTrue(result is UiState.Error)
        assertEquals(AppError.NoConnection, (result as UiState.Error).error)
    }

    @Test
    fun `maps HttpRequestTimeoutException to Timeout`() = runBlocking {
        val result = safeApiCall<String> {
            throw HttpRequestTimeoutException(HttpRequestBuilder().apply { url("https://test") })
        }
        assertTrue(result is UiState.Error)
        assertEquals(AppError.Timeout, (result as UiState.Error).error)
    }

    @Test
    fun `maps SerializationException to Serialization`() = runBlocking {
        val result = safeApiCall<String> { throw SerializationException("bad json") }
        assertTrue(result is UiState.Error)
        assertTrue((result as UiState.Error).error is AppError.Serialization)
    }

    @Test
    fun `maps arbitrary Throwable to Unknown`() = runBlocking {
        val result = safeApiCall<String> { error("boom") }
        assertTrue(result is UiState.Error)
        assertTrue((result as UiState.Error).error is AppError.Unknown)
    }

    @Test(expected = CancellationException::class)
    fun `rethrows CancellationException so coroutines can be cancelled`(): Unit = runBlocking {
        safeApiCall<String> { throw CancellationException("cancelled") }
        Unit
    }
}
