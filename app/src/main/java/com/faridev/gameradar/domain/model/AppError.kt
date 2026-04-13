package com.faridev.gameradar.domain.model

/**
 * Typed error model shared across layers.
 *
 * The data layer maps raw exceptions (Ktor, IO, serialization) into one of these variants,
 * and the UI layer turns each variant into a localizable, user-friendly message.
 */
sealed class AppError {
    data object NoConnection : AppError()
    data object Timeout : AppError()
    data class Http(val code: Int, val reason: String? = null) : AppError()
    data class Serialization(val detail: String? = null) : AppError()
    data class Unknown(val detail: String? = null) : AppError()

    /** Human-readable default. Kept in the model so UI and logs stay in sync. */
    val userMessage: String
        get() = when (this) {
            NoConnection -> "You're offline. Check your connection and try again."
            Timeout -> "The request timed out. Please try again."
            is Http -> when (code) {
                401, 403 -> "You're not authorized to make this request."
                404 -> "We couldn't find what you were looking for."
                in 500..599 -> "The server is having trouble. Please try again shortly."
                else -> "Request failed (HTTP $code)."
            }
            is Serialization -> "Received an unexpected response from the server."
            is Unknown -> "Something went wrong. Please try again."
        }
}
