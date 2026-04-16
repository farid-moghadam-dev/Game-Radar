package com.faridev.gameradar.data.remote

import com.faridev.gameradar.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import timber.log.Timber

object KtorClientFactory {
    private const val BASE_URL = "https://api.rawg.io/api/"

    fun create(): HttpClient =
        HttpClient(Android) {
            install(Logging) {
                level = LogLevel.INFO
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Timber.tag("Ktor").d(message)
                        println(message)
                    }
                }
            }

            install(ContentNegotiation) {
                json(
                    json = kotlinx.serialization.json.Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (!response.status.isSuccess()) {
                        val errorBody = response.bodyAsText()
                        throw ResponseException(response, errorBody)
                    }
                }
            }

            // this block runs for every request
            defaultRequest {
                url {
                    takeFrom(BASE_URL)
                    parameters.append("key", BuildConfig.API_KEY)
                }
            }
        }
}
