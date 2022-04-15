package com.emenjivar.pomodoro.screens.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emenjivar.core.model.Pomodoro
import com.emenjivar.core.usecase.GetPomodoroUseCase
import com.emenjivar.core.usecase.SetWorkTimeUseCase
import com.emenjivar.core.usecase.SetRestTimeUseCase
import com.emenjivar.pomodoro.MainCoroutineRule
import com.emenjivar.pomodoro.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private lateinit var getPomodoroUseCase: GetPomodoroUseCase
    private lateinit var setWorkTimeUseCase: SetWorkTimeUseCase
    private lateinit var setRestTimeUseCase: SetRestTimeUseCase

    private lateinit var settingsViewModel: SettingsViewModel

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun prepareTest() {
        getPomodoroUseCase = Mockito.mock(GetPomodoroUseCase::class.java)
        setWorkTimeUseCase = Mockito.mock(SetWorkTimeUseCase::class.java)
        setRestTimeUseCase = Mockito.mock(SetRestTimeUseCase::class.java)

        settingsViewModel = SettingsViewModel(
            getPomodoroUseCase = getPomodoroUseCase,
            setPomodoroTimeUseCase = setWorkTimeUseCase,
            setRestTimeUseCase = setRestTimeUseCase,
            ioDispatcher = Dispatchers.Main,
            testMode = true
        )
    }

    @Test
    fun `loadSettings test`() = runTest {
        // Given 25 and 5 minutes
        Mockito.`when`(getPomodoroUseCase.invoke()).thenReturn(
            Pomodoro(workTime = 1500000L, restTime = 300000L)
        )

        // When
        settingsViewModel.loadSettings()

        // Then verify the values are loaded in readable minutes
        assertEquals(25, settingsViewModel.pomodoroTime.getOrAwaitValue())
        assertEquals(5, settingsViewModel.restTime.getOrAwaitValue())
    }

    @Test
    fun `setPomodoroTime using string parameter`() {
        settingsViewModel.setPomodoroTime("10")
        assertEquals(10L, settingsViewModel.pomodoroTime.getOrAwaitValue())
    }

    @Test
    fun `setPomodoroTime using an invalid string parameter`() {
        settingsViewModel.setPomodoroTime("NaN")
        assertEquals(0L, settingsViewModel.pomodoroTime.getOrAwaitValue())
    }

    @Test
    fun `setRestTime using string parameter`() {
        settingsViewModel.setRestTime("10")
        assertEquals(10L, settingsViewModel.restTime.getOrAwaitValue())
    }

    @Test
    fun `setRestTime using an invalid string parameter`() {
        settingsViewModel.setRestTime("NaN")
        assertEquals(0L, settingsViewModel.restTime.getOrAwaitValue())
    }

    @Test
    fun `closeSettings test`() {
        settingsViewModel.closeSettings()

        val result = settingsViewModel.closeSettings.getOrAwaitValue()
        Assert.assertTrue(result)
    }
}
