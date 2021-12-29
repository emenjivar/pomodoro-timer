package com.emenjivar.pomodoro.screens.countdown

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emenjivar.pomodoro.getOrAwaitValue
import com.emenjivar.pomodoro.model.Pomodoro
import com.emenjivar.pomodoro.utils.TimerUtility
import org.junit.Assert.*
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
        assertEquals(Pomodoro(), viewModel.counter.value)
        assertFalse(viewModel.isPlaying.value ?: true)
        assertFalse(viewModel.isFullScreen.value ?: true)
        assertTrue(viewModel.testMode)
    }

    @Test
    fun `setTime changes liveData using default pomodoro values`() {
        val time = TimerUtility.POMODORO_TIME
        viewModel.setTime(time)

        val counter = viewModel.counter.getOrAwaitValue()
        assertEquals(time, counter.milliseconds)
        assertEquals("25:00", counter.time)
        assertEquals(1f, counter.progress)
    }

    @Test
    fun `startTime changes liveData value`() {
        viewModel.startTimer()

        val isPlaying = viewModel.isPlaying.getOrAwaitValue()
        assertTrue(isPlaying)
    }

    @Test
    fun `pauseTimer changes liveData values`() {
        viewModel.pauseTimer()

        val isPlaying = viewModel.isPlaying.getOrAwaitValue()
        assertFalse(isPlaying)
    }

    @Test
    fun `stopTimer changes liveData values`() {
        viewModel.stopTimer()

        val isPlaying = viewModel.isPlaying.getOrAwaitValue()
        val counter = viewModel.counter.getOrAwaitValue()

        assertFalse(isPlaying)
        assertEquals(Pomodoro(), counter)
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
}