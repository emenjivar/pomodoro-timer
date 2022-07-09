package com.emenjivar.pomodoro.screens.settings.time

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emenjivar.core.usecase.GetPomodoroUseCase
import com.emenjivar.core.usecase.SetRestTimeUseCase
import com.emenjivar.core.usecase.SetWorkTimeUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

class SettingsTimeViewModelTest {

    private lateinit var settingsTimeViewModel: SettingsTimeViewModel
    private lateinit var getPomodoroUseCase: GetPomodoroUseCase
    private lateinit var setWorkTimeUseCase: SetWorkTimeUseCase
    private lateinit var setRestTimeUseCase: SetRestTimeUseCase

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        getPomodoroUseCase = Mockito.mock(GetPomodoroUseCase::class.java)
        setWorkTimeUseCase = Mockito.mock(SetWorkTimeUseCase::class.java)
        setRestTimeUseCase = Mockito.mock(SetRestTimeUseCase::class.java)

        settingsTimeViewModel = SettingsTimeViewModel(
            getPomodoroUseCase = getPomodoroUseCase,
            setWorkTimeUseCase = setWorkTimeUseCase,
            setRestTimeUseCase = setRestTimeUseCase
        )
    }

    @Test
    fun `onInputChange normal behavior`() {
        with(settingsTimeViewModel) {
            // Given a defined time of 25:10:49

            // When input the digits one by one
            // Then verify the hours, minutes and seconds
            // has the correct values
            onInputChange(2)
            assertEquals("00", hours.value)
            assertEquals("00", minutes.value)
            assertEquals("02", seconds.value)

            onInputChange(5)
            assertEquals("00", hours.value)
            assertEquals("00", minutes.value)
            assertEquals("25", seconds.value)

            onInputChange(1)
            assertEquals("00", hours.value)
            assertEquals("02", minutes.value)
            assertEquals("51", seconds.value)

            onInputChange(0)
            assertEquals("00", hours.value)
            assertEquals("25", minutes.value)
            assertEquals("10", seconds.value)

            onInputChange(4)
            assertEquals("02", hours.value)
            assertEquals("51", minutes.value)
            assertEquals("04", seconds.value)

            // Then verify the final time
            onInputChange(9)
            assertEquals("25", hours.value)
            assertEquals("10", minutes.value)
            assertEquals("49", seconds.value)
        }
    }

    @Test
    fun `onInputChange when function is called more than 6 times should keep just the first 6 digits`() {
        with(settingsTimeViewModel) {
            // Given a defined time of 25:10:49
            onInputChange(2)
            onInputChange(5)
            onInputChange(1)
            onInputChange(0)
            onInputChange(4)
            onInputChange(9)

            // When function receive more than 6 characters
            onInputChange(9)
            onInputChange(9)
            onInputChange(9)

            // Then verify the last 3 extra characters are ignored
            assertEquals("25", hours.value)
            assertEquals("10", minutes.value)
            assertEquals("49", seconds.value)
        }
    }

    @Test
    fun `getMilliseconds normal behavior`() {
        with(settingsTimeViewModel) {
            // Given a defined time of 25:10:49
            onInputChange(2)
            onInputChange(5)
            onInputChange(1)
            onInputChange(0)
            onInputChange(4)
            onInputChange(9)

            // 25:10:49 in milliseconds
            val milliseconds: Long = 1000 * (3600 * 25 + 60 * 10 + 49)
            // Then verify the total milliseconds
            assertEquals(milliseconds, getMilliseconds())
        }
    }

    @Test
    fun `getMilliseconds when time does not follow the normal time units`() {
        with(settingsTimeViewModel) {
            // Given a defined time 0f 99:99:99
            onInputChange(9)
            onInputChange(9)
            onInputChange(9)
            onInputChange(9)
            onInputChange(9)
            onInputChange(9)

            // 99:99:99 in milliseconds
            val milliseconds: Long = 1000 * (3600 * 99 + 60 * 99 + 99)

            // Then verify the total of milliseconds
            assertEquals(milliseconds, getMilliseconds())
        }
    }

    @Test
    fun `onBackSpace should delete the last digit of the string`() {
        with(settingsTimeViewModel) {
            // Given a defined time of 25:10:49
            onInputChange(2)
            onInputChange(5)
            onInputChange(1)
            onInputChange(0)
            onInputChange(4)
            onInputChange(9)

            // When the last digit is deleted
            onBackSpace()

            // Then verify the time is 02:51:04
            assertEquals("02", hours.value)
            assertEquals("51", minutes.value)
            assertEquals("04", seconds.value)
        }
    }

    @Test
    fun `onBackSpace when all time is deleted`() {
        with(settingsTimeViewModel) {
            // Given a defined time of 25:10:49
            onInputChange(2)
            onInputChange(5)
            onInputChange(1)
            onInputChange(0)
            onInputChange(4)
            onInputChange(9)

            // When complete time string is deleted
            onBackSpace()
            onBackSpace()
            onBackSpace()
            onBackSpace()
            onBackSpace()
            onBackSpace()

            // Then verify the values are 0
            assertEquals("00", hours.value)
            assertEquals("00", minutes.value)
            assertEquals("00", seconds.value)
        }
    }

    @Test
    fun `onBackSpace trying to delete an empty date should keep values in 0`() {
        with(settingsTimeViewModel) {
            // First verify the values are 0
            assertEquals("00", hours.value)
            assertEquals("00", minutes.value)
            assertEquals("00", seconds.value)

            // When onBackSpace function is called many times
            onBackSpace()
            onBackSpace()
            onBackSpace()

            // Then verify the values are still 0
            assertEquals("00", hours.value)
            assertEquals("00", minutes.value)
            assertEquals("00", seconds.value)
        }
    }

    @Test
    fun `loadMillisecondsTime should load a long milliseconds value on viewModel state`() {
        with(settingsTimeViewModel) {
            // Given a defined time of 25:10:49
            val milliseconds: Long = 1000 * (3600 * 25 + 10 * 60 + 49)

            // When
            loadMillisecondsTime(milliseconds)

            // Then verify the data is loaded
            assertEquals("25", hours.value)
            assertEquals("10", minutes.value)
            assertEquals("49", seconds.value)
        }
    }
}
