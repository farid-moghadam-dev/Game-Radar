package com.faridev.gameradar.core.util

import androidx.annotation.FontRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun getFontFamilyFromRes(
    @FontRes fontRes: Int,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal
): FontFamily {
    return FontFamily(
        fonts = listOf(
            Font(
                resId = fontRes,
                weight = fontWeight,
                style = fontStyle
            )
        )
    )
}

inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}

@OptIn(ExperimentalFoundationApi::class)
inline fun Modifier.noRippleCombinedClickable(
    crossinline onClick: () -> Unit,
    crossinline onLongClick: () -> Unit,
): Modifier = composed {
    combinedClickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = { onClick() },
        onLongClick = { onLongClick() }
    )
}

class NoRippleInteractionSource() : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = true
}
