package com.emenjivar.pomodoro.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class TimerUtilsTests {

    @Test
    fun counterInterval_firstInterval() {
        val counterFiveMinutes = countDownInterval(300000)
        val counterFourMinutes = countDownInterval(240000)
        val counterThreeMinutes = countDownInterval(180000)
        val counterTwoMinutes = countDownInterval(120000)
        val counterOneMinute = countDownInterval(60000)

        assertEquals(500L, counterFiveMinutes)
        assertEquals(500L, counterFourMinutes)
        assertEquals(500L, counterThreeMinutes)
        assertEquals(500L, counterTwoMinutes)
        assertEquals(500L, counterOneMinute)
    }

    @Test
    fun counterInterval_secondInterval() {
        // 5:00 min + 1 ms
        val counter = countDownInterval(300001)
        assertEquals(1000L, counter)
    }
}
