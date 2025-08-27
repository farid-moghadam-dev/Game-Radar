package com.faridev.gameradar.presentation.feature

import kotlinx.serialization.Serializable

sealed class NavRoutes {
    @Serializable
    object Home

    @Serializable
    data class Detail(val gameId: Int)
}