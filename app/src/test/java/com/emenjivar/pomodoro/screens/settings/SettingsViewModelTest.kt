package com.emenjivar.pomodoro.screens.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emenjivar.core.usecase.*
import com.emenjivar.pomodoro.MainCoroutineRule
import com.emenjivar.pomodoro.getOrAwaitValue
import com.emenjivar.pomodoro.system.CustomVibrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private lateinit var getColorUseCase: GetColorUseCase
    private lateinit var setColorUseCase: SetColorUseCase
    private lateinit var getAutoPlayUseCase: GetAutoPlayUseCase
    private lateinit var setAutoPlayUseCase: SetAutoPlayUseCase
    private lateinit var isKeepScreenOnUseCase: IsKeepScreenOnUseCase
    private lateinit var setKeepScreenOnUseCase: SetKeepScreenOnUseCase
    private lateinit var isVibrationEnabledUseCase: IsVibrationEnabledUseCase
    private lateinit var setVibrationUseCase: SetVibrationUseCase
    private lateinit var areSoundsEnableUseCase: AreSoundsEnableUseCase
    private lateinit var setSoundsEnableUseCase: SetSoundsEnableUseCase
    private lateinit var customVibrator: CustomVibrator
    private lateinit var settingsViewModel: SettingsViewModel

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun prepareTest() {
        getColorUseCase = Mockito.mock(GetColorUseCase::class.java)
        setColorUseCase = Mockito.mock(SetColorUseCase::class.java)
        getAutoPlayUseCase = Mockito.mock(GetAutoPlayUseCase::class.java)
        setAutoPlayUseCase = Mockito.mock(SetAutoPlayUseCase::class.java)
        isKeepScreenOnUseCase = Mockito.mock(IsKeepScreenOnUseCase::class.java)
        setKeepScreenOnUseCase = Mockito.mock(SetKeepScreenOnUseCase::class.java)
        isVibrationEnabledUseCase = Mockito.mock(IsVibrationEnabledUseCase::class.java)
        setVibrationUseCase = Mockito.mock(SetVibrationUseCase::class.java)
        areSoundsEnableUseCase = Mockito.mock(AreSoundsEnableUseCase::class.java)
        setSoundsEnableUseCase = Mockito.mock(SetSoundsEnableUseCase::class.java)
        customVibrator = Mockito.mock(CustomVibrator::class.java)

        settingsViewModel = SettingsViewModel(
            getColorUseCase = getColorUseCase,
            setColorUseCase = setColorUseCase,
            getAutoPlayUseCase = getAutoPlayUseCase,
            setAutoPlayUseCase = setAutoPlayUseCase,
            isKeepScreenOnUseCase = isKeepScreenOnUseCase,
            setKeepScreenOnUseCase = setKeepScreenOnUseCase,
            isVibrationEnabledUseCase = isVibrationEnabledUseCase,
            setVibrationUseCase = setVibrationUseCase,
            areSoundsEnableUseCase = areSoundsEnableUseCase,
            setSoundsEnableUseCase = setSoundsEnableUseCase,
            customVibrator = customVibrator,
            ioDispatcher = Dispatchers.Main,
            testMode = true
        )
    }

    @Test
    fun defaultValues() {
        with(settingsViewModel) {
            assertFalse(closeSettings.value ?: true)
            assertFalse(autoPlay.value)
            assertFalse(keepScreenOn.value)
            assertFalse(vibrationEnabled.value)
            assertTrue(soundsEnable.value)
            assertNull(selectedColor.value)
        }
    }

    @Test
    fun `loadSettings test`() = runTest {
        val color = 10

        Mockito.`when`(getColorUseCase.invoke())
            .thenReturn(color)
        Mockito.`when`(getAutoPlayUseCase.invoke())
            .thenReturn(true)
        Mockito.`when`(isKeepScreenOnUseCase.invoke())
            .thenReturn(true)
        Mockito.`when`(isVibrationEnabledUseCase.invoke())
            .thenReturn(true)
        Mockito.`when`(areSoundsEnableUseCase.invoke())
            .thenReturn(false)

        with(settingsViewModel) {
            // When
            settingsViewModel.loadSettings()

            // Then verify the values are loaded in readable minutes
            assertEquals(color, selectedColor.getOrAwaitValue())
            assertTrue(autoPlay.value)
            assertTrue(keepScreenOn.value)
            assertTrue(vibrationEnabled.value)
            assertFalse(soundsEnable.value)
        }
    }

    @Test
    fun `setColor test`() = runTest {
        val color = 10
        var clicked = false
        var localSelectedColor: Int? = null

        with(settingsViewModel) {
            Mockito.`when`(customVibrator.click()).then {
                clicked = true
                it
            }
            Mockito.`when`(setColorUseCase.invoke(color)).then {
                localSelectedColor = it.getArgument(0)
                it
            }
            setColor(color)

            assertEquals(color, selectedColor.getOrAwaitValue())
            assertTrue(clicked)
            assertEquals(color, localSelectedColor)
        }
    }

    @Test
    fun `setAutoPlay test`() = runTest {
        with(settingsViewModel) {
            var localAutoPlay = false

            Mockito.`when`(setAutoPlayUseCase.invoke(true))
                .then {
                    localAutoPlay = true
                    it
                }
            Mockito.`when`(setAutoPlayUseCase.invoke(false))
                .then {
                    localAutoPlay = false
                    it
                }

            setAutoPlay(true)
            assertTrue(autoPlay.value)
            assertTrue(localAutoPlay)

            setAutoPlay(false)
            assertFalse(autoPlay.value)
            assertFalse(localAutoPlay)
        }
    }

    @Test
    fun `setKeepScreenOn test`() = runTest {
        with(settingsViewModel) {
            var localKeepScreenOn = false

            Mockito.`when`(setKeepScreenOnUseCase.invoke(Mockito.anyBoolean()))
                .then {
                    // Getting the useCase argument and set in a local var
                    localKeepScreenOn = it.getArgument(0)
                    it
                }

            setKeepScreenOn(true)
            assertTrue(keepScreenOn.value)
            assertTrue(localKeepScreenOn)

            setKeepScreenOn(false)
            assertFalse(keepScreenOn.value)
            assertFalse(localKeepScreenOn)
        }
    }

    @Test
    fun `setVibration test`() = runTest {
        var localVibration = false

        Mockito.`when`(setVibrationUseCase.invoke(Mockito.anyBoolean()))
            .then {
                // Getting the useCase argument and set in a local var
                localVibration = it.getArgument(0)
                it
            }

        with(settingsViewModel) {
            setVibration(true)
            assertTrue(vibrationEnabled.value)
            assertTrue(localVibration)

            setVibration(false)
            assertFalse(vibrationEnabled.value)
            assertFalse(localVibration)
        }
    }

    @Test
    fun `setSoundsEnable test`() = runTest {
        var localSoundsEnable = false

        Mockito.`when`(setSoundsEnableUseCase.invoke(Mockito.anyBoolean()))
            .then {
                // Getting the useCase argument and set in a local var
                localSoundsEnable = it.getArgument(0)
                it
            }

        with(settingsViewModel) {
            setSoundsEnable(true)
            assertTrue(soundsEnable.value)
            assertTrue(localSoundsEnable)

            setSoundsEnable(false)
            assertFalse(soundsEnable.value)
            assertFalse(localSoundsEnable)
        }
    }

    @Test
    fun `closeSettings test`() {
        settingsViewModel.closeSettings()

        val result = settingsViewModel.closeSettings.getOrAwaitValue()
        assertTrue(result)
    }
}
