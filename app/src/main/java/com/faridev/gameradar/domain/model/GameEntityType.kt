package com.faridev.gameradar.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class GameEntityType(
    val apiPath: String,
    val gamesFilterParam: String,
    val displayName: String,
) {
    Platforms(apiPath = "platforms", gamesFilterParam = "platforms", displayName = "Platforms"),
    Publishers(apiPath = "publishers", gamesFilterParam = "publishers", displayName = "Publishers"),
    Developers(apiPath = "developers", gamesFilterParam = "developers", displayName = "Developers"),
    Genres(apiPath = "genres", gamesFilterParam = "genres", displayName = "Genres"),
    Stores(apiPath = "stores", gamesFilterParam = "stores", displayName = "Stores"),
}
