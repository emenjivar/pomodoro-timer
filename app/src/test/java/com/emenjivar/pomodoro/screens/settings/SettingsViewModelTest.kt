package com.emenjivar.pomodoro.screens.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emenjivar.pomodoro.getOrAwaitValue
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SettingsViewModelTest {

    private val settingsViewModel = SettingsViewModel()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `test default values`() {
        assertFalse(settingsViewModel.closeSettings.value ?: true)
    }

    @Test
    fun `closeSettings test`() {
        settingsViewModel.closeSettings()

        val result = settingsViewModel.closeSettings.getOrAwaitValue()
        Assert.assertTrue(result)
    }
}
