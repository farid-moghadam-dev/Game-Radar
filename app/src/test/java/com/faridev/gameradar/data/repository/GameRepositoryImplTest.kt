package com.faridev.gameradar.data.repository

import com.faridev.gameradar.data.remote.GameApi
import com.faridev.gameradar.domain.model.AppError
import com.faridev.gameradar.domain.model.GameEntityType
import com.faridev.gameradar.presentation.common.state.UiState
import com.faridev.gameradar.testutil.testHttpClient
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameRepositoryImplTest {

    private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")

    @Test
    fun `fetchGamesList returns Success and hits the games endpoint`() = runTest {
        val recorded = mutableListOf<HttpRequestData>()
        val client = testHttpClient(recorded) {
            respond(
                content = ByteReadChannel(GAMES_LIST_JSON),
                status = HttpStatusCode.OK,
                headers = jsonHeaders,
            )
        }
        val repo = GameRepositoryImpl(GameApi(client))

        val result = repo.fetchGamesList(page = 1, pageSize = 20)

        assertTrue(result is UiState.Success)
        val domain = (result as UiState.Success).data
        assertEquals(1, domain.count)
        assertEquals("Grand Theft Auto V", domain.results.single().name)

        val url = recorded.single().url
        assertTrue(url.encodedPath.endsWith("/games"))
        assertEquals("1", url.parameters["page"])
        assertEquals("20", url.parameters["page_size"])
        assertEquals("test", url.parameters["key"])
    }

    @Test
    fun `fetchGamesList maps HTTP 500 to AppError_Http`() = runTest {
        val client = testHttpClient {
            respondError(HttpStatusCode.InternalServerError)
        }
        val repo = GameRepositoryImpl(GameApi(client))

        val result = repo.fetchGamesList(page = 1, pageSize = 20)

        assertTrue(result is UiState.Error)
        val error = (result as UiState.Error).error
        assertTrue(error is AppError.Http)
        assertEquals(500, (error as AppError.Http).code)
    }

    @Test
    fun `fetchEntityDetails hits the typed entity endpoint`() = runTest {
        val recorded = mutableListOf<HttpRequestData>()
        val client = testHttpClient(recorded) {
            respond(
                content = ByteReadChannel(DEVELOPER_DETAILS_JSON),
                status = HttpStatusCode.OK,
                headers = jsonHeaders,
            )
        }
        val repo = GameRepositoryImpl(GameApi(client))

        val result = repo.fetchEntityDetails(GameEntityType.Developers, entityId = 1612)

        assertTrue(result is UiState.Success)
        val domain = (result as UiState.Success).data
        assertEquals(1612, domain.id)
        assertEquals("Valve Software", domain.name)

        assertTrue(recorded.single().url.encodedPath.endsWith("/developers/1612"))
    }

    companion object {
        private val GAMES_LIST_JSON = """
            {
              "count": 1,
              "next": null,
              "previous": null,
              "results": [
                {
                  "id": 3498,
                  "name": "Grand Theft Auto V",
                  "background_image": "gta.jpg",
                  "metacritic": 92,
                  "parent_platforms": []
                }
              ]
            }
        """.trimIndent()

        private val DEVELOPER_DETAILS_JSON = """
            {
              "id": 1612,
              "name": "Valve Software",
              "slug": "valve-software",
              "games_count": 44,
              "image_background": "img.jpg",
              "description": ""
            }
        """.trimIndent()
    }
}
