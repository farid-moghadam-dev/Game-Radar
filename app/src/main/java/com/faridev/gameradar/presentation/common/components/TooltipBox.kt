package com.faridev.gameradar.presentation.common.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTextTooltip(
    tooltipText: String,
    tooltipContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    tooltipTextColor: Color = Color.Unspecified,
    tooltipTextFontSize: TextUnit = TextUnit.Unspecified,
    tooltipTextFontStyle: FontStyle? = null,
    tooltipTextFontWeight: FontWeight? = null,
    tooltipTextFontFamily: FontFamily? = null,
    tooltipTextLetterSpacing: TextUnit = TextUnit.Unspecified,
    tooltipTextTextDecoration: TextDecoration? = null,
    tooltipTextAlign: TextAlign? = null,
    tooltipTextLineHeight: TextUnit = TextUnit.Unspecified,
    tooltipTextSoftWrap: Boolean = true,
    tooltipTextMaxLines: Int = Int.MAX_VALUE,
    tooltipTextMinLines: Int = 1,
    tooltipTextOnTextLayout: ((TextLayoutResult) -> Unit)? = null,
    tooltipTextStyle: TextStyle = LocalTextStyle.current,
    tooltipBoxContent: @Composable (tooltipState: TooltipState, tooltipScope: CoroutineScope) -> Unit
) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(4.dp),
        tooltip = {
            RichTooltip(
                caretSize = TooltipDefaults.caretSize,
                colors = TooltipDefaults.richTooltipColors(
                    containerColor = tooltipContainerColor
                )
            ) {
                Text(
                    text = tooltipText,
                    color = tooltipTextColor,
                    fontSize = tooltipTextFontSize,
                    fontStyle = tooltipTextFontStyle,
                    fontWeight = tooltipTextFontWeight,
                    fontFamily = tooltipTextFontFamily,
                    letterSpacing = tooltipTextLetterSpacing,
                    textDecoration = tooltipTextTextDecoration,
                    textAlign = tooltipTextAlign,
                    lineHeight = tooltipTextLineHeight,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = tooltipTextSoftWrap,
                    maxLines = tooltipTextMaxLines,
                    minLines = tooltipTextMinLines,
                    onTextLayout = tooltipTextOnTextLayout,
                    style = tooltipTextStyle,
                )
            }
        },
        state = tooltipState,
        content = {
            tooltipBoxContent(tooltipState, scope)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDefaultTextTooltip(
    tooltipText: String,
    tooltipBoxContent: @Composable (tooltipState: TooltipState, tooltipScope: CoroutineScope) -> Unit
) {
    ShowTextTooltip(
        tooltipText = tooltipText,
        tooltipContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        tooltipTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
        tooltipTextFontSize = 13.sp,
        tooltipBoxContent = tooltipBoxContent
    )
}