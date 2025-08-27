package com.faridev.gameradar.presentation.common.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator

/**
 * Show an Animated Text that iterate by characters and animate one by one.
 * @param text : Define text that should show in Animated Text.
 * @param typingDelayInMs : Define how many milliseconds between each character should pause for.
 * @param startDelayInMs : Initial start delay of the typing animation.
 */
@Composable
fun AnimatedText(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = TextStyle(),
    typingDelayInMs: Long = 100L,
    startDelayInMs: Long = 1000
) {
    // Use BreakIterator as it correctly iterates over characters regardless of how they are
    // stored, for example, some emojis are made up of multiple characters.
    // You don't want to break up an emoji as it animates, so using BreakIterator will ensure
    // this is correctly handled!
    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }
    var substringText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        delay(startDelayInMs)
        breakIterator.text = StringCharacterIterator(text)

        var nextIndex = breakIterator.next()
        // Iterate over the string, by index boundary
        while (nextIndex != BreakIterator.DONE) {
            substringText = text.subSequence(0, nextIndex).toString()
            // Go to the next logical character boundary
            nextIndex = breakIterator.next()
            delay(typingDelayInMs)
        }
    }
    Text(
        modifier = modifier,
        text = substringText,
        style = textStyle
    )
}