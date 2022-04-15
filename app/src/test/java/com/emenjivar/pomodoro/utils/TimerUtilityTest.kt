package com.emenjivar.pomodoro.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class TimerUtilityTest {

    private val workTime = 1500000L

    @Test
    fun `formatTime parse complete pomodoro time`() {
        assertEquals("25:00", workTime.formatTime())
    }

    @Test
    fun `formatTime parse pomodoro time less one second`() {
        val time = workTime - 1000
        assertEquals("24:59", time.formatTime())
    }

    @Test
    fun `formatTime parse pomodoro time less 1 minute, 45 seconds`() {
        val times = workTime - (1000 * 60 + 1000 * 45)
        assertEquals("23:15", times.formatTime())
    }

    @Test
    fun `formatTime parse zero time`() {
        val time = 0L
        assertEquals(time.formatTime(), "00:00")
    }

    @Test
    fun `formatTime parse half pomodoro time`() {
        val time = workTime / 2
        assertEquals(time.formatTime(), "12:30")
    }

    @Test
    fun `formatTime parse double pomodoro time`() {
        val time = workTime * 2
        assertEquals(time.formatTime(), "50:00")
    }
}
