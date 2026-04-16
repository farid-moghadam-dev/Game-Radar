package com.faridev.gameradar.data.mapper

import com.faridev.gameradar.data.model.GamesListResDto
import com.faridev.gameradar.data.model.ParentPlatformDto
import org.junit.Assert.assertEquals
import org.junit.Test

class GameListMapperTest {

    @Test
    fun `maps dto results into domain GameResult list`() {
        val dto = GamesListResDto(
            count = 1,
            next = null,
            previous = null,
            results = listOf(
                GamesListResDto.Result(
                    id = 3498,
                    name = "Grand Theft Auto V",
                    backgroundImage = "gta.jpg",
                    metacritic = 92,
                    parentPlatforms = listOf(
                        ParentPlatformDto(
                            platform = ParentPlatformDto.Platform(id = 1, name = "PC", slug = "pc"),
                        ),
                    ),
                ),
            ),
        )

        val domain = dto.toDomain()

        assertEquals(1, domain.count)
        assertEquals(1, domain.results.size)
        val first = domain.results.first()
        assertEquals("Grand Theft Auto V", first.name)
        assertEquals(92, first.metacritic)
        assertEquals("PC", first.parentPlatforms.single().platform?.name)
    }

    @Test
    fun `missing optional fields do not blow up`() {
        val dto = GamesListResDto(
            count = 0,
            results = listOf(
                GamesListResDto.Result(id = 1, name = "Minimal"),
            ),
        )

        val domain = dto.toDomain()

        assertEquals("Minimal", domain.results.single().name)
        assertEquals(null, domain.results.single().metacritic)
        assertEquals(emptyList<Any>(), domain.results.single().parentPlatforms)
    }
}
