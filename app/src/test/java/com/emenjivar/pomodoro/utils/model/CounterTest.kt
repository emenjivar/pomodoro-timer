package com.emenjivar.pomodoro.utils.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CounterTest {

    private val workTime = 1500000L // 25 min
    private val restTime = 300000L // 5 min

    @Test
    fun `100 percent progress on work phase`() {
        val counter = Counter(
            workTime = workTime,
            restTime = restTime,
            phase = Phase.WORK,
            countDown = workTime
        )
        assertEquals(100f, counter.getProgress())
        assertEquals(1f, counter.getScaleProgress())
    }

    @Test
    fun `complete progress minus one second`() {
        val counter = Counter(
            workTime = workTime,
            restTime = 300000,
            phase = Phase.WORK,
            countDown = workTime - 1000 // 24:59 min
        )
        assertEquals(99.933334f, counter.getProgress())
        assertEquals(0.99933334f, counter.getScaleProgress())
    }

    @Test
    fun `half progress on work phase`() {
        val counter = Counter(
            workTime = workTime,
            restTime = restTime,
            phase = Phase.WORK,
            countDown = workTime / 2
        )

        assertEquals(50.0f, counter.getProgress())
        assertEquals(0.5f, counter.getScaleProgress())
    }

    @Test
    fun `zero progress`() {
        val counter = Counter(
            workTime = workTime,
            restTime = restTime,
            phase = Phase.WORK,
            countDown = 0
        )

        assertEquals(0f, counter.getProgress())
        assertEquals(0f, counter.getScaleProgress())
    }

    @Test
    fun `setRest test`() {
        // Given a counter in work state
        val counter = Counter(
            workTime = workTime,
            restTime = restTime,
            phase = Phase.WORK,
            countDown = workTime
        )

        // Move to rest phase
        counter.setRest()

        // Check rest values are set
        assertEquals(Phase.REST, counter.phase)
        assertEquals(restTime, counter.countDown)
    }

    @Test
    fun `100 percent progress on rest state`() {
        // Given a counter in rest state
        val counter = Counter(
            workTime = workTime,
            restTime = restTime,
            phase = Phase.REST,
            countDown = restTime
        )

        assertEquals(100f, counter.getProgress())
        assertEquals(1f, counter.getScaleProgress())
    }

    @Test
    fun `50 percent progress on rest state`() {
        // Given a counter in rest state using 50% countdown
        val counter = Counter(
            workTime = workTime,
            restTime = restTime,
            phase = Phase.WORK,
            countDown = workTime / 2
        )

        assertEquals(50f, counter.getProgress())
        assertEquals(0.5f, counter.getScaleProgress())
    }

    @Test
    fun `0 percent progress on rest state`() {
        // Given a counter in rest state using 50% countdown
        val counter = Counter(
            workTime = workTime,
            restTime = restTime,
            phase = Phase.WORK,
            countDown = 0
        )

        assertEquals(0f, counter.getProgress())
        assertEquals(0f, counter.getScaleProgress())
    }
}
