package com.faridev.gameradar.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AppErrorTest {

    @Test
    fun `NoConnection maps to offline message`() {
        assertEquals(
            "You're offline. Check your connection and try again.",
            AppError.NoConnection.userMessage,
        )
    }

    @Test
    fun `Timeout maps to retry message`() {
        assertEquals("The request timed out. Please try again.", AppError.Timeout.userMessage)
    }

    @Test
    fun `Http 401 maps to unauthorized`() {
        assertEquals(
            "You're not authorized to make this request.",
            AppError.Http(401).userMessage,
        )
    }

    @Test
    fun `Http 404 maps to not found`() {
        assertEquals(
            "We couldn't find what you were looking for.",
            AppError.Http(404).userMessage,
        )
    }

    @Test
    fun `Http 5xx maps to server message`() {
        assertEquals(
            "The server is having trouble. Please try again shortly.",
            AppError.Http(503).userMessage,
        )
    }

    @Test
    fun `Http other maps to generic with code`() {
        assertEquals("Request failed (HTTP 418).", AppError.Http(418).userMessage)
    }
}
