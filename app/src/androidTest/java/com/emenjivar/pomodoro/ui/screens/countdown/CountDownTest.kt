package com.emenjivar.pomodoro.ui.screens.countdown

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.emenjivar.pomodoro.model.Phase
import com.emenjivar.pomodoro.utils.Action
import org.junit.Rule
import org.junit.Test

class CountDownTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun countDown_work_phase() {
        composeTestRule.setContent {
            CountDown(
                time = "5:00",
                progress = 50f
            )
        }
        composeTestRule.onNodeWithText("5:00").assertExists()
        composeTestRule.onNodeWithText("Work time").assertExists()
    }

    @Test
    fun countDown_rest_phase() {
        composeTestRule.setContent {
            CountDown(
                time = "5:00",
                progress = 50f,
                phase = Phase.REST
            )
        }
        composeTestRule.onNodeWithText("5:00").assertExists()
        composeTestRule.onNodeWithText("Rest time").assertExists()
    }

    @Test
    fun countDown_paused() {
        composeTestRule.setContent {
            CountDown(
                time = "",
                progress = 0f,
                action = Action.Pause
            )
        }
        composeTestRule.onNodeWithText("Paused").assertIsDisplayed()
    }
}
