package com.faridev.gameradar.data.mapper

import com.faridev.gameradar.data.model.GameEntityDetailsResDto
import com.faridev.gameradar.data.model.GameEntityListResDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GameEntityMapperTest {

    @Test
    fun `list DTO maps every field including nested top games`() {
        val dto = GameEntityListResDto(
            count = 2,
            next = "next-url",
            previous = null,
            results = listOf(
                GameEntityListResDto.Result(
                    id = 1612,
                    name = "Valve Software",
                    slug = "valve-software",
                    gamesCount = 44,
                    imageBackground = "img.jpg",
                    games = listOf(
                        GameEntityListResDto.Result.Game(id = 4200, name = "Portal 2", slug = "portal-2"),
                        GameEntityListResDto.Result.Game(id = 13536, name = "Portal", slug = "portal"),
                    ),
                ),
            ),
        )

        val domain = dto.toDomain()

        assertEquals(2, domain.count)
        assertEquals("next-url", domain.next)
        assertNull(domain.previous)
        assertEquals(1, domain.results.size)

        val entity = domain.results.single()
        assertEquals(1612, entity.id)
        assertEquals("Valve Software", entity.name)
        assertEquals(44, entity.gamesCount)
        assertEquals(2, entity.topGames.size)
        assertEquals("Portal 2", entity.topGames.first().name)
    }

    @Test
    fun `details DTO passes domain and description through`() {
        val dto = GameEntityDetailsResDto(
            id = 1,
            name = "Steam",
            slug = "steam",
            gamesCount = 123_329,
            imageBackground = "img.jpg",
            description = "<p>Steam is cool</p>",
            domain = "store.steampowered.com",
        )

        val domain = dto.toDomain()

        assertEquals("Steam", domain.name)
        assertEquals(123_329, domain.gamesCount)
        assertEquals("store.steampowered.com", domain.domain)
        assertEquals("<p>Steam is cool</p>", domain.description)
    }
}
