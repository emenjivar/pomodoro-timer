package com.emenjivar.pomodoro.screens.countdown

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emenjivar.core.repository.SettingsRepository
import com.emenjivar.core.usecase.GetPomodoroTimeUseCase
import com.emenjivar.core.usecase.GetRestTimeUseCase
import com.emenjivar.core.usecase.IsNightModeUseCase
import com.emenjivar.core.usecase.SetNighModeUseCase
import com.emenjivar.pomodoro.getOrAwaitValue
import com.emenjivar.pomodoro.model.NormalPomodoro
import com.emenjivar.pomodoro.model.RestPomodoro
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class CountDownViewModelTest {

    private lateinit var getPomodoroTimeUseCase: GetPomodoroTimeUseCase
    private lateinit var getRestTimeUseCase: GetRestTimeUseCase
    private lateinit var setNighModeUseCase: SetNighModeUseCase
    private lateinit var isNightModeUseCase: IsNightModeUseCase
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: CountDownViewModel

    private var nightMode = true

    // Given a defined time of 25 minutes
    private val time = 1500000L

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() = runTest {
        settingsRepository = Mockito.mock(SettingsRepository::class.java)
        getPomodoroTimeUseCase = GetPomodoroTimeUseCase(settingsRepository)
        getRestTimeUseCase = GetRestTimeUseCase(settingsRepository)
        setNighModeUseCase = SetNighModeUseCase(settingsRepository)
        isNightModeUseCase = IsNightModeUseCase(settingsRepository)

        Mockito.`when`(isNightModeUseCase.invoke()).thenReturn(nightMode)
        Mockito.`when`(getPomodoroTimeUseCase.invoke())
            .thenReturn(time)
        Mockito.`when`(getRestTimeUseCase.invoke())
            .thenReturn(time)

        viewModel = CountDownViewModel(
            getPomodoroTimeUseCase = getPomodoroTimeUseCase,
            getRestTimeUseCase = getRestTimeUseCase,
            setNighModeUseCase = setNighModeUseCase,
            isNightModeUseCase = isNightModeUseCase,
            testMode = true
        )
    }

    @Test
    fun `test default values`() {
        assertEquals(NormalPomodoro(), viewModel.pomodoro.value)
        assertFalse(viewModel.isPlaying.value ?: true)
        assertTrue(viewModel.isNightMode.value ?: true)
        assertFalse(viewModel.openSettings.value ?: true)
        assertTrue(viewModel.startForBeginning)
    }

    @Test
    fun `loadDefaultValues test`() = runTest {
        with(viewModel) {
            loadDefaultValues()
            val nightMode = isNightMode.getOrAwaitValue()

            assertTrue(nightMode)
            assertEquals(2, listPomodoro.size)
            assertTrue(listPomodoro.poll() is NormalPomodoro)
            assertTrue(listPomodoro.poll() is RestPomodoro)
        }
    }

    @Test
    fun `startTimer when pomodoro is null`() = runTest {
        // Given a 25 minutes configuration
        Mockito.`when`(getPomodoroTimeUseCase.invoke())
            .thenReturn(time)

        // When the function receives an empty parameter
        viewModel.startTimer()

        // Then check the playing flag is stop
        assertFalse(viewModel.isPlaying.getOrAwaitValue())

        with(viewModel.pomodoro.value) {
            // And check the default object is loaded
            assertEquals(time, this?.milliseconds)
            assertEquals(time, this?.totalMilliseconds)
            assertEquals("25:00", this?.time)
            assertEquals(100f, this?.progress)
        }
    }

    @Test
    fun `startTimer when pomodoro is not null`() {
        // Given a not-null value for startTimer parameter
        viewModel.startTimer(NormalPomodoro())

        // Then verify the flag is true
        assertTrue(viewModel.isPlaying.getOrAwaitValue())
        assertTrue(viewModel.pomodoro.getOrAwaitValue() is NormalPomodoro)
    }

    @Test
    fun `playTimer when startForBeginning flag is true`() {
        with(viewModel) {
            // Given a new type of loaded pomodoro
            listPomodoro.clear()
            listPomodoro.add(RestPomodoro())
            startForBeginning = true

            // When
            playTimer()

            // Check
            assertFalse(startForBeginning)
            assertTrue(isPlaying.getOrAwaitValue())
            assertNotNull(pomodoro.getOrAwaitValue())
        }
    }

    @Test
    fun `playTimer when startForBeginning flag is false`() {

        with(viewModel) {
            // Given a flag set on false
            startForBeginning = false

            // When
            playTimer()

            // Then check the livedata
            assertFalse(startForBeginning)
            assertTrue(isPlaying.getOrAwaitValue())
            assertTrue(pomodoro.getOrAwaitValue() is NormalPomodoro)
        }
    }

    @Test
    fun `pauseTimer normal behavior`() {
        with(viewModel) {
            pauseTimer()
            assertFalse(isPlaying.getOrAwaitValue())
        }
    }

    @Test
    fun `nextPomodoro invoked until queue is empty`() {
        with(viewModel) {
            // Given a list of n pomodoro
            listPomodoro.clear()
            listPomodoro.add(NormalPomodoro())
            listPomodoro.add(RestPomodoro())

            // When method is called many times
            nextPomodoro()
            nextPomodoro()

            // Then check the list
            assertTrue(listPomodoro.isEmpty())
        }
    }

    @Test
    fun `setTime changes liveData using default pomodoro values`() {
        // Given a normal pomodoro
        val pomodoro = NormalPomodoro()

        // When livedata is loaded
        viewModel.setTime(pomodoro, pomodoro.milliseconds)

        // Then check if match
        val counter = viewModel.pomodoro.getOrAwaitValue()
        assertEquals(pomodoro.milliseconds, counter.milliseconds)
        assertEquals("25:00", counter.time)
        assertEquals(1f, counter.progress)
    }

    @Test
    fun `stopCurrentPomodoro test`() {
        with(viewModel) {
            stopCurrentPomodoro()

            // Then
            assertFalse(isPlaying.getOrAwaitValue())
            assertTrue(this.pomodoro.getOrAwaitValue() is NormalPomodoro)
        }
    }

    @Test
    fun `toggleNightMode changes liveData value`() = runTest {
        val first = viewModel.isNightMode.getOrAwaitValue()
        viewModel.toggleNightMode()

        val second = viewModel.isNightMode.getOrAwaitValue()
        viewModel.toggleNightMode()

        val third = viewModel.isNightMode.getOrAwaitValue()

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
