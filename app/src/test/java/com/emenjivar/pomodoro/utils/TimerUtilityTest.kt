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
        assertEquals("00", time.formatTime())
    }

    @Test
    fun `formatTime parse half pomodoro time`() {
        val time = workTime / 2
        assertEquals("12:30", time.formatTime())
    }

    @Test
    fun `formatTime parse double pomodoro time`() {
        val time = workTime * 2
        assertEquals("50:00", time.formatTime())
    }

    @Test
    fun `formatTime parsing less than 1 minute should display seconds only`() {
        // 59 secs
        val time: Long = 1000 * 59
        assertEquals("59", time.formatTime())
    }

    @Test
    fun `formatTime parsing between 1 minutes and 1 hour should displays mm ss format`() {
        // 1 min
        val oneMinute: Long = 1000 * 60

        // 59min:59sec
        val almostOneHour: Long = 1000 * (59 * 60 + 59)

        assertEquals("01:00", oneMinute.formatTime())
        assertEquals("59:59", almostOneHour.formatTime())
    }

    @Test
    fun `formatTime parsing more or equal than 1 hour should displays hh mm ss format`() {
        // Given a defined time of 25:10:49
        val milliseconds: Long = 1000 * (3600 * 25 + 10 * 60 + 49)
        assertEquals("25:10:49", milliseconds.formatTime())
    }

    @Test
    fun `formatTime parsing a null value should return default format`() {
        val empty: Long? = null
        assertEquals("00:00:00", empty.formatTime())
    }
}
