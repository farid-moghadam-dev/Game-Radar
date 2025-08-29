package com.faridev.gameradar.presentation.common.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.TextUnit
import com.faridev.gameradar.presentation.common.theme.DarkHyperlinkColor

@Composable
fun ClickableWordsText(
    modifier: Modifier = Modifier,
    text: String,
    normalTextColor: Color = Color.Unspecified,
    linkTextColor: Color = DarkHyperlinkColor,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    onWordClick: (String) -> Unit
) {
    val annotatedString = buildAnnotatedString {
        val words = text.split(Regex(",+"))
        words.forEachIndexed { index, word ->
            if (word.isNotEmpty()) {
                val clickableWord = LinkAnnotation.Clickable(
                    tag = word,
                    styles = TextLinkStyles(
                        style = SpanStyle(color = linkTextColor),
                        pressedStyle = SpanStyle(color = linkTextColor, textDecoration = TextDecoration.Underline),
                    ),
                    linkInteractionListener = { it ->
                        onWordClick((it as LinkAnnotation.Clickable).tag)
                    }
                )
                withLink(clickableWord) { append(word) }
            }

            if (index != words.lastIndex) {
                append(", ")
            }
        }
    }

    Text(
        modifier = modifier,
        text = annotatedString,
        color = normalTextColor,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        style = style,
    )
}