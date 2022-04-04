package com.emenjivar.pomodoro.screens.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emenjivar.core.repository.SettingsRepository
import com.emenjivar.core.usecase.GetPomodoroTimeUseCase
import com.emenjivar.core.usecase.GetRestTimeUseCase
import com.emenjivar.core.usecase.SetPomodoroTimeUseCase
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

    private lateinit var getPomodoroTimeUseCase: GetPomodoroTimeUseCase
    private lateinit var setPomodoroTimeUseCase: SetPomodoroTimeUseCase
    private lateinit var getRestTimeUseCase: GetRestTimeUseCase
    private lateinit var setRestTimeUseCase: SetRestTimeUseCase
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var settingsViewModel: SettingsViewModel

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun prepareTest() = runTest {
        settingsRepository = Mockito.mock(SettingsRepository::class.java)
        getPomodoroTimeUseCase = Mockito.mock(GetPomodoroTimeUseCase::class.java)
        setPomodoroTimeUseCase = SetPomodoroTimeUseCase(settingsRepository)
        getRestTimeUseCase = Mockito.mock(GetRestTimeUseCase::class.java)
        setRestTimeUseCase = SetRestTimeUseCase(settingsRepository)

        settingsViewModel = SettingsViewModel(
            getPomodoroTimeUseCase = getPomodoroTimeUseCase,
            setPomodoroTimeUseCase = setPomodoroTimeUseCase,
            getRestTimeUseCase = getRestTimeUseCase,
            setRestTimeUseCase = setRestTimeUseCase,
            ioDispatcher = Dispatchers.Main,
            testMode = true
        )
    }

    @Test
    fun `loadSettings test`() = runTest {
        // Given 25 and 5 minutes
        Mockito.`when`(getPomodoroTimeUseCase.invoke()).thenReturn(1500000L)
        Mockito.`when`(getRestTimeUseCase.invoke()).thenReturn(300000L)

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
