package com.faridev.gameradar.presentation.feature

import com.faridev.gameradar.domain.model.GameEntityType
import kotlinx.serialization.Serializable

sealed class NavRoutes {
    @Serializable
    object Home

    @Serializable
    data class Detail(val gameId: Int)

    @Serializable
    data class EntityList(val type: GameEntityType)

    @Serializable
    data class EntityDetail(val type: GameEntityType, val entityId: Int)
}
