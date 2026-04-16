package com.faridev.gameradar.testutil

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://api.rawg.io/api/"

/**
 * Build a test [HttpClient] configured like the production one but driven by a
 * [MockEngine] handler. Use the [recorded] list to assert the outgoing requests.
 */
fun testHttpClient(
    recorded: MutableList<HttpRequestData> = mutableListOf(),
    handler: MockRequestHandler,
): HttpClient {
    val engineConfig = MockEngineConfig().apply {
        addHandler { request ->
            recorded.add(request)
            handler(request)
        }
    }
    return HttpClient(MockEngine(engineConfig)) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                },
            )
        }
        defaultRequest {
            url {
                takeFrom(BASE_URL)
                parameters.append("key", "test")
            }
        }
    }
}
