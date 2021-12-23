package com.emenjivar.pomodoro.utils

import com.emenjivar.pomodoro.utils.TimerUtility.formatTime
import org.junit.Assert.assertEquals
import org.junit.Test

class TimerUtilityTest{

    @Test
    fun `formatTime parse complete pomodoro time`() {
        val time = TimerUtility.POMODORO_TIME
        val progress = TimerUtility.getProgress(time)

        assertEquals("25:00", time.formatTime())
        assertEquals(100L, progress)
    }

    @Test
    fun `formatTime parse pomodoro time less one second`() {
        val time = TimerUtility.POMODORO_TIME - 1000
        val progress = TimerUtility.getProgress(time)

        assertEquals("24:59", time.formatTime())
        assertEquals(99L, progress)
    }

    @Test
    fun `formatTime parse pomodoro time less 1 minute, 45 seconds`() {
        val times = TimerUtility.POMODORO_TIME - (1000 * 60 + 1000 * 45)
        assertEquals("23:15", times.formatTime())
    }

    @Test
    fun `formatTime parse zero time`() {
        val time = 0L
        assertEquals(time.formatTime(), "00:00")
    }

    @Test
    fun `formatTime parse half pomodoro time`() {
        val time  = TimerUtility.POMODORO_TIME / 2
        val progress = TimerUtility.getProgress(time)

        assertEquals(time.formatTime(), "12:30")
        assertEquals(50L, progress)
    }

    @Test
    fun `formatTime parse double pomodoro time`() {
        val time = TimerUtility.POMODORO_TIME * 2
        val progress = TimerUtility.getProgress(time, time)

        assertEquals(time.formatTime(), "50:00")
        assertEquals(100L, progress)
    }

    @Test
    fun `getProgress calculate several percents`() {
        val time100Percent = TimerUtility.getProgress(TimerUtility.POMODORO_TIME)
        val time75Percent = TimerUtility.getProgress((TimerUtility.POMODORO_TIME * 0.75).toLong())
        val time50Percent = TimerUtility.getProgress(TimerUtility.POMODORO_TIME / 2)
        val time25Percent = TimerUtility.getProgress((TimerUtility.POMODORO_TIME * 0.25).toLong())
        val time0Percent = TimerUtility.getProgress(0L)

        assertEquals(100L, time100Percent)
        assertEquals(75L, time75Percent)
        assertEquals(50L, time50Percent)
        assertEquals(25L, time25Percent)
        assertEquals(0L, time0Percent)
    }
}