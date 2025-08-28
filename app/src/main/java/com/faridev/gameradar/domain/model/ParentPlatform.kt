package com.faridev.gameradar.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class ParentPlatform(
    val platform: ParentPlatformInfo?
)

@Immutable
data class ParentPlatformInfo(
    val id: Int?,
    val name: String?,
    val slug: String?
)