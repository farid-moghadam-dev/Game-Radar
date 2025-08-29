package com.faridev.gameradar.presentation.common.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.faridev.gameradar.core.util.noRippleClickable

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    softWrap: Boolean = true,
    maxLines: Int = 3,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
    readMoreCardColor : Color = MaterialTheme.colorScheme.secondaryContainer,
    readMoreTextColor : Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .noRippleClickable { isExpanded = !isExpanded }
            .animateContentSize()
            .then(modifier),
    ) {
        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = TextOverflow.Ellipsis,
            softWrap = softWrap,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style,
        )

        Card(modifier = Modifier.padding(top = 4.dp), colors = CardDefaults.cardColors(containerColor = readMoreCardColor, contentColor = readMoreTextColor), shape = RoundedCornerShape(4.dp)) {
            Text(
                text = if (isExpanded) "Show less" else "Read more",
                color = color,
                style = style,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(3.dp)
            )
        }
    }
}