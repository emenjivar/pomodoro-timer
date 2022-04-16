package com.emenjivar.pomodoro.screens.countdown

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emenjivar.core.model.Pomodoro
import com.emenjivar.core.usecase.GetAutoPlayUseCase
import com.emenjivar.core.usecase.GetPomodoroUseCase
import com.emenjivar.core.usecase.IsNightModeUseCase
import com.emenjivar.core.usecase.SetNighModeUseCase
import com.emenjivar.pomodoro.MainCoroutineRule
import com.emenjivar.pomodoro.getOrAwaitValue
import com.emenjivar.pomodoro.model.Phase
import com.emenjivar.pomodoro.utils.Action
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
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
    private lateinit var getAutoPlayUseCase: GetAutoPlayUseCase
    private lateinit var isNightModeUseCase: IsNightModeUseCase
    private lateinit var viewModel: CountDownViewModel

    private var nightMode = true

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() = runTest {
        getPomodoroUseCase = Mockito.mock(GetPomodoroUseCase::class.java)
        setNighModeUseCase = Mockito.mock(SetNighModeUseCase::class.java)
        getAutoPlayUseCase = Mockito.mock(GetAutoPlayUseCase::class.java)
        isNightModeUseCase = Mockito.mock(IsNightModeUseCase::class.java)

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
            getAutoPlayUseCase = getAutoPlayUseCase,
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
            assertFalse(autoPlay)
        }
    }

    @Test
    fun `loadDefaultValues test`() = runTest {
        with(viewModel) {
            Mockito.`when`(getAutoPlayUseCase.invoke())
                .thenReturn(false)

            loadDefaultValues()

            assertTrue(isNightMode.value)
            assertFalse(autoPlay)
            assertEquals(25000L, counter.value?.workTime)
            assertEquals(5000L, counter.value?.restTime)
        }
    }

    @Test
    fun `initCounter test`() = runTest {
        with(viewModel) {
            Mockito.`when`(getPomodoroUseCase.invoke())
                .thenReturn(
                    Pomodoro(
                        workTime = 25000L,
                        restTime = 5000L
                    )
                )

            assertNull(counter.value)

            initCounter()
            assertEquals(25000L, counter.value?.workTime)
            assertEquals(5000L, counter.value?.restTime)
        }
    }

    @Test
    fun `startCounter when counter is null`() = runTest {

        // When the function receives an empty parameter
        viewModel.startCounter()

        // Then check the current action
        assertEquals(Action.Play, viewModel.action.getOrAwaitValue())
    }

    @Test
    fun `pauseCounter test`() {
        with(viewModel) {
            pauseCounter()
            assertEquals(Action.Pause, action.value)
        }
    }

    @Test
    fun `resumeCounter test`() {
        with(viewModel) {
            resumeCounter()
            assertEquals(Action.Resume, action.value)
        }
    }

    @Test
    fun `stopCounter test`() {
        with(viewModel) {
            stopCounter()
            assertEquals(action.value, Action.Stop)
            assertEquals(25000L, counter.value?.workTime)
            assertEquals(5000L, counter.value?.restTime)
        }
    }

    @Test
    fun `finishCounter when phase is WORK`() = runTest {
        with(viewModel) {
            initCounter()
            assertEquals(Phase.WORK, counter.value?.phase)
            finishCounter()
            assertEquals(Phase.REST, counter.value?.phase)
        }
    }

    @Test
    fun `finishCounter when phase is REST`() = runTest {
        with(viewModel) {
            initCounter()
            counter.value?.setRest()

            assertEquals(Phase.REST, counter.value?.phase)
            finishCounter()
            assertNull(counter.value)
            assertEquals(action.value, Action.Stop)
        }
    }

    @Test
    fun `restartCounter when autoPlay is true`() {
        with(viewModel) {
            autoPlay = true

            restartCounter()
            assertEquals(action.value, Action.Play)
            assertNotNull(counter.value)
        }
    }

    @Test
    fun `restartCounter when autoPlay is false`() = runTest {
        with(viewModel) {
            Mockito.`when`(getPomodoroUseCase.invoke())
                .thenReturn(
                    Pomodoro(
                        workTime = 0L,
                        restTime = 0L
                    )
                )

            autoPlay = false
            restartCounter()

            assertEquals(0L, counter.value?.workTime)
            assertEquals(0L, counter.value?.restTime)
            assertEquals(action.value, Action.Stop)
        }
    }

    @Test
    fun `setTime test`() = runTest {
        with(viewModel) {
            Mockito.`when`(getAutoPlayUseCase.invoke())
                .thenReturn(true)

            // Load counter value
            loadDefaultValues()

            setTime(10000)
            assertTrue(autoPlay)
            assertEquals(10000L, counter.value?.countDown)
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
