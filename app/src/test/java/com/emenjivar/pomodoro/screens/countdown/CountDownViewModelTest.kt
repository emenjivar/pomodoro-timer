package com.emenjivar.pomodoro.screens.countdown

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emenjivar.pomodoro.getOrAwaitValue
import com.emenjivar.pomodoro.model.NormalPomodoro
import com.emenjivar.pomodoro.model.RestPomodoro
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CountDownViewModelTest {

    private val viewModel = CountDownViewModel()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel.testMode = true
    }

    @Test
    fun `test default values`() {
        assertEquals(NormalPomodoro(), viewModel.pomodoro.value)
        assertFalse(viewModel.isPlaying.value ?: true)
        assertFalse(viewModel.isFullScreen.value ?: true)
        assertFalse(viewModel.openSettings.value ?: true)
        assertTrue(viewModel.listPomodoro.isNotEmpty())
        assertTrue(viewModel.startForBeginning)
        assertTrue(viewModel.testMode)
    }

    @Test
    fun `default pomodoro list`() {
        // Given a default list
        // Then check the size and type of object
        assertEquals(2, viewModel.listPomodoro.size)
        assertTrue(viewModel.listPomodoro.poll() is NormalPomodoro)
        assertTrue(viewModel.listPomodoro.poll() is RestPomodoro)
    }

    @Test
    fun `startTimer when pomodoro is null`() {
        // Given a default pomodoro
        val pomodoro = NormalPomodoro()

        // When
        viewModel.startTimer()

        with(viewModel.pomodoro.value) {
            // Then check the data match with default object
            assertEquals(pomodoro.milliseconds, this?.milliseconds)
            assertEquals(pomodoro.totalMilliseconds, this?.totalMilliseconds)
            assertEquals(pomodoro.time, this?.time)
            assertEquals(1f, this?.progress)
        }
    }

    @Test
    fun `startTimer when pomodoro is not null`() {
        viewModel.startTimer(NormalPomodoro())
        assertTrue(viewModel.isPlaying.getOrAwaitValue())
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
            assertTrue(pomodoro.getOrAwaitValue() is RestPomodoro)
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
            listPomodoro.add(NormalPomodoro())
            listPomodoro.add(RestPomodoro())

            // When method is called many times
            nextPomodoro()
            nextPomodoro()
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
    fun `stopCurrentPomodoro when data is NormalPomodoro`() {
        // Given a normal pomodoro
        val pomodoro = NormalPomodoro()

        with(viewModel) {
            // When livedata is loaded and stop is invoked
            startTimer(pomodoro)
            stopCurrentPomodoro()

            // Then
            assertFalse(isPlaying.getOrAwaitValue())
            assertTrue(this.pomodoro.getOrAwaitValue() is NormalPomodoro)
        }
    }

    @Test
    fun `stopCurrentPomodoro when data is RestPomodoro`() {
        // Given a rest pomodoro
        val pomodoro = RestPomodoro()

        with(viewModel) {
            // When livedata is loaded and stop is invoked
            startTimer(pomodoro)
            stopCurrentPomodoro()

            // Then
            assertFalse(isPlaying.getOrAwaitValue())
            assertTrue(this.pomodoro.getOrAwaitValue() is RestPomodoro)
        }
    }

    @Test
    fun `toggleNightMode changes liveData value`() {
        val first = viewModel.isFullScreen.getOrAwaitValue()
        viewModel.toggleNightMode()

        val second = viewModel.isFullScreen.getOrAwaitValue()
        viewModel.toggleNightMode()

        val third = viewModel.isFullScreen.getOrAwaitValue()

        assertFalse(first)
        assertTrue(second)
        assertFalse(third)
    }

    @Test
    fun `openSettings changes liveData value`() {
        viewModel.openSettings()

        val result = viewModel.openSettings.getOrAwaitValue()
        assertTrue(result)
    }
}
