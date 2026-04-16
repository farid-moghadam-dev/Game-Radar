package com.faridev.gameradar.presentation.common.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ErrorItemTest {

    @Rule
    @JvmField
    val composeTestRule = createComposeRule()

    @Test
    fun rendersMessageAndTriggersRetryOnClick() {
        var retryCount = 0

        composeTestRule.setContent {
            ErrorItem(
                message = "You're offline. Check your connection and try again.",
                onRetry = { retryCount++ },
            )
        }

        composeTestRule
            .onNodeWithText("You're offline. Check your connection and try again.")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Retry").performClick()

        assertEquals(1, retryCount)
    }
}
