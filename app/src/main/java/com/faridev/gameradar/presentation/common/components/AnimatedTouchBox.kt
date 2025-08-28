package com.faridev.gameradar.presentation.common.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun AnimatedTouchBox(
    modifier: Modifier = Modifier,
    overlayColor: Color = Color.Black.copy(alpha = 0.1f),
    rippleColor: Color = Color.Unspecified,
    animationDuration: Int = 150,
    shape: Shape = RectangleShape,
    onClick: (() -> Unit)? = null,
    pressScale : Float = 1.3f,
    backgroundContent: @Composable () -> Unit,
    foregroundContent: @Composable BoxScope.(State<Boolean>) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()

    val overlayAlpha = animateFloatAsState(
        targetValue = if (isPressed.value) overlayColor.alpha else 0f,
        animationSpec = tween(animationDuration),
        label = "overlay_alpha"
    )

    val pressScale = animateFloatAsState(
        targetValue = if (isPressed.value) pressScale else 1f,
        animationSpec = tween(animationDuration),
        label = "press_scale"
    )

    Box(
        modifier = modifier
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = rippleColor)
            ) { onClick?.invoke() }
    ) {
        // Static Background Content + animated overlay
        BackgroundWithOverlay(
            overlayColor = overlayColor,
            overlayAlpha = overlayAlpha,
            scale = pressScale,
            content = backgroundContent
        )

        // Foreground content
        ForegroundContent(
            isPressed = isPressed,
            content = foregroundContent
        )
    }
}

@Composable
private fun BackgroundWithOverlay(
    overlayColor: Color,
    overlayAlpha: State<Float>,
    scale: State<Float>,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .drawWithContent {
                drawContent()
                drawRect(color = overlayColor.copy(alpha = overlayAlpha.value))
            }
    ) {
        content()
    }
}

@Composable
private fun BoxScope.ForegroundContent(
    isPressed: State<Boolean>,
    content: @Composable BoxScope.(State<Boolean>) -> Unit
) {
    Box(modifier = Modifier.matchParentSize()) {
        content(isPressed)
    }
}
