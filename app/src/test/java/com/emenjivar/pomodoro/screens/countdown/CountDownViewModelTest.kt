package com.emenjivar.pomodoro.screens.countdown

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emenjivar.core.model.Pomodoro
import com.emenjivar.core.repository.SettingsRepository
import com.emenjivar.core.usecase.GetPomodoroUseCase
import com.emenjivar.core.usecase.IsNightModeUseCase
import com.emenjivar.core.usecase.SetNighModeUseCase
import com.emenjivar.pomodoro.MainCoroutineRule
import com.emenjivar.pomodoro.getOrAwaitValue
import com.emenjivar.pomodoro.utils.Action
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class CountDownViewModelTest {

    private lateinit var getPomodoroUseCase: GetPomodoroUseCase
    private lateinit var setNighModeUseCase: SetNighModeUseCase
    private lateinit var isNightModeUseCase: IsNightModeUseCase
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: CountDownViewModel

    private var nightMode = true

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() = runTest {
        settingsRepository = Mockito.mock(SettingsRepository::class.java)
        getPomodoroUseCase = GetPomodoroUseCase(settingsRepository)
        setNighModeUseCase = SetNighModeUseCase(settingsRepository)
        isNightModeUseCase = IsNightModeUseCase(settingsRepository)

        Mockito.`when`(isNightModeUseCase.invoke()).thenReturn(nightMode)
        Mockito.`when`(getPomodoroUseCase.invoke()).thenReturn(
            Pomodoro(
                workTime = 25000,
                restTime = 5000
            )
        )

        viewModel = CountDownViewModel(
            getPomodoroUseCase = getPomodoroUseCase,
            setNighModeUseCase = setNighModeUseCase,
            isNightModeUseCase = isNightModeUseCase,
            ioDispatcher = Dispatchers.Main,
            testMode = true
        )
    }

    @Test
    fun `test default values`() {
        with(viewModel) {
            assertNull(counter.value)
            assertEquals(Action.Stop, action.value)
            assertTrue(isNightMode.value)
            assertFalse(openSettings.value ?: true)
        }
    }

    @Test
    fun `loadDefaultValues test`() = runTest {
        with(viewModel) {
            loadDefaultValues()
            assertTrue(isNightMode.value)
            assertEquals(25000L, counter.value?.workTime)
            assertEquals(5000L, counter.value?.restTime)
        }
    }

    @Test
    fun `startCounter when counter is null`() = runTest {

        // When the function receives an empty parameter
        viewModel.startCounter()

        // Then check the current action
        assertEquals(viewModel.action.getOrAwaitValue(), Action.Play)
    }

    @Test
    fun `pauseCounter test`() {
        with(viewModel) {
            pauseCounter()
            assertEquals(action.value, Action.Pause)
        }
    }

    @Test
    fun `stopCounter test`() {
        with(viewModel) {
            stopCounter()
            assertEquals(action.value, Action.Stop)
            assertEquals(counter.value?.workTime, 25000L)
            assertEquals(counter.value?.restTime, 5000L)
        }
    }

    @Test
    fun `setTime test`() = runTest {
        with(viewModel) {
            // Load counter value
            loadDefaultValues()

            setTime(10000)
            assertEquals(counter.value?.countDown, 10000L)
        }
    }

    @Test
    fun `toggleNightMode changes liveData value`() = runTest {
        val first = viewModel.isNightMode.value
        viewModel.toggleNightMode()

        val second = viewModel.isNightMode.value
        viewModel.toggleNightMode()

        val third = viewModel.isNightMode.value

        assertTrue(first)
        assertFalse(second)
        assertTrue(third)
    }

    @Test
    fun `openSettings changes liveData value`() {
        viewModel.openSettings()

        val result = viewModel.openSettings.getOrAwaitValue()
        assertTrue(result)
    }
}
