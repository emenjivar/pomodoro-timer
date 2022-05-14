package com.emenjivar.pomodoro.screens.countdown

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emenjivar.core.model.Pomodoro
import com.emenjivar.core.usecase.AreSoundsEnableUseCase
import com.emenjivar.core.usecase.GetAutoPlayUseCase
import com.emenjivar.core.usecase.GetColorUseCase
import com.emenjivar.core.usecase.GetPomodoroUseCase
import com.emenjivar.core.usecase.IsKeepScreenOnUseCase
import com.emenjivar.core.usecase.IsNightModeUseCase
import com.emenjivar.core.usecase.IsVibrationEnabledUseCase
import com.emenjivar.core.usecase.SetNighModeUseCase
import com.emenjivar.pomodoro.MainCoroutineRule
import com.emenjivar.pomodoro.getOrAwaitValue
import com.emenjivar.pomodoro.model.Phase
import com.emenjivar.pomodoro.system.CustomNotificationManager
import com.emenjivar.pomodoro.system.CustomVibrator
import com.emenjivar.pomodoro.system.SoundsManager
import com.emenjivar.pomodoro.utils.Action
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class CountDownViewModelTest {

    private lateinit var getColorUseCase: GetColorUseCase
    private lateinit var getPomodoroUseCase: GetPomodoroUseCase
    private lateinit var setNighModeUseCase: SetNighModeUseCase
    private lateinit var getAutoPlayUseCase: GetAutoPlayUseCase
    private lateinit var isNightModeUseCase: IsNightModeUseCase
    private lateinit var isKeepScreenOnUseCase: IsKeepScreenOnUseCase
    private lateinit var isVibrationEnabledUseCase: IsVibrationEnabledUseCase
    private lateinit var areSoundsEnableUseCase: AreSoundsEnableUseCase
    private lateinit var notificationManager: CustomNotificationManager
    private lateinit var customVibrator: CustomVibrator
    private lateinit var soundsManager: SoundsManager
    private lateinit var viewModel: CountDownViewModel

    private var nightMode = true

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() = runTest {
        getColorUseCase = Mockito.mock(GetColorUseCase::class.java)
        getPomodoroUseCase = Mockito.mock(GetPomodoroUseCase::class.java)
        setNighModeUseCase = Mockito.mock(SetNighModeUseCase::class.java)
        getAutoPlayUseCase = Mockito.mock(GetAutoPlayUseCase::class.java)
        isNightModeUseCase = Mockito.mock(IsNightModeUseCase::class.java)
        isKeepScreenOnUseCase = Mockito.mock(IsKeepScreenOnUseCase::class.java)
        isVibrationEnabledUseCase = Mockito.mock(IsVibrationEnabledUseCase::class.java)
        areSoundsEnableUseCase = Mockito.mock(AreSoundsEnableUseCase::class.java)
        notificationManager = Mockito.mock(CustomNotificationManager::class.java)
        customVibrator = Mockito.mock(CustomVibrator::class.java)
        soundsManager = Mockito.mock(SoundsManager::class.java)

        Mockito.`when`(isNightModeUseCase.invoke()).thenReturn(nightMode)
        Mockito.`when`(getPomodoroUseCase.invoke()).thenReturn(
            Pomodoro(
                workTime = 25000,
                restTime = 5000
            )
        )
        Mockito.`when`(isKeepScreenOnUseCase.invoke()).thenReturn(false)

        viewModel = CountDownViewModel(
            getColorUseCase = getColorUseCase,
            getPomodoroUseCase = getPomodoroUseCase,
            setNighModeUseCase = setNighModeUseCase,
            getAutoPlayUseCase = getAutoPlayUseCase,
            isNightModeUseCase = isNightModeUseCase,
            isKeepScreenOnUseCase = isKeepScreenOnUseCase,
            isVibrationEnabledUseCase = isVibrationEnabledUseCase,
            notificationManager = notificationManager,
            areSoundsEnableUseCase = areSoundsEnableUseCase,
            customVibrator = customVibrator,
            soundsManager = soundsManager,
            ioDispatcher = Dispatchers.Main,
            testMode = true
        )
    }

    @Test
    fun `test default values`() {
        with(viewModel) {
            assertNull(counter.value)
            assertEquals(Action.Stop, action.value)
            assertTrue(isNightMode.value)
            assertFalse(openSettings.value ?: true)
            assertFalse(autoPlay)
            assertNull(keepScreenOn.value)
            assertNull(selectedColor.value)
            assertFalse(vibrationEnabled)
            assertFalse(displayNotification)
            assertTrue(areSoundsEnable)
        }
    }

    @Test
    fun `loadDefaultValues test`() = runTest {
        with(viewModel) {
            Mockito.`when`(getColorUseCase.invoke())
                .thenReturn(1)
            Mockito.`when`(getAutoPlayUseCase.invoke())
                .thenReturn(false)
            Mockito.`when`(isKeepScreenOnUseCase.invoke())
                .thenReturn(false)
            Mockito.`when`(isVibrationEnabledUseCase.invoke())
                .thenReturn(true)

            loadDefaultValues()

            assertEquals(1, selectedColor.value)
            assertTrue(isNightMode.value)
            assertFalse(autoPlay)
            assertTrue(vibrationEnabled)
            assertFalse(keepScreenOn.getOrAwaitValue() ?: true)
            assertEquals(25000L, counter.value?.workTime)
            assertEquals(5000L, counter.value?.restTime)
        }
    }

    @Test
    fun `initCounter test`() = runTest {
        with(viewModel) {
            Mockito.`when`(getPomodoroUseCase.invoke())
                .thenReturn(
                    Pomodoro(
                        workTime = 25000L,
                        restTime = 5000L
                    )
                )

            assertNull(counter.value)

            initCounter()
            assertEquals(25000L, counter.value?.workTime)
            assertEquals(5000L, counter.value?.restTime)
        }
    }

    @Test
    fun `startCounter when counter is null`() = runTest {

        // When the function receives an empty parameter
        viewModel.startCounter()

        // Then check the current action
        assertEquals(Action.Play, viewModel.action.getOrAwaitValue())
    }

    @Test
    fun `pauseCounter when displayNotification is false`() {
        with(viewModel) {
            pauseCounter()
            assertEquals(Action.Pause, action.value)
        }
    }

    @Test
    fun `pauseCounter when displayNotification is true`() {
        with(viewModel) {
            pauseCounter()
            assertEquals(Action.Pause, action.value)
        }
    }

    @Test
    fun `resumeCounter when displayNotification is false`() {
        with(viewModel) {
            displayNotification = false

            resumeCounter()
            assertEquals(Action.Resume, action.value)
        }
    }

    @Test
    fun `resumeCounter when displayNotification is true`() = runTest {
        with(viewModel) {
            initCounter()
            var localAction: Action = Action.Resume

            displayNotification = true

            counter.value?.let { safeCounter ->
                Mockito.`when`(
                    notificationManager.updateProgress(
                        safeCounter,
                        Action.Play
                    )
                ).then {
                    localAction = Action.Play
                    null
                }
            }

            resumeCounter()
            assertEquals(Action.Resume, action.value)
            assertEquals(Action.Play, localAction)
        }
    }

    @Test
    fun `stopCounter test`() {
        with(viewModel) {
            var isNotificationClosed = false

            Mockito.`when`(notificationManager.close())
                .then {
                    isNotificationClosed = true
                    null
                }

            stopCounter()
            assertEquals(action.value, Action.Stop)
            assertEquals(25000L, counter.value?.workTime)
            assertEquals(5000L, counter.value?.restTime)
            assertTrue(isNotificationClosed)
        }
    }

    @Test
    fun `finishCounter when phase is WORK`() = runTest {
        with(viewModel) {
            initCounter()
            assertEquals(Phase.WORK, counter.value?.phase)
            finishCounter()
            assertEquals(Phase.REST, counter.value?.phase)
        }
    }

    @Test
    fun `finishCounter when phase is REST`() = runTest {
        with(viewModel) {
            initCounter()
            counter.value?.setRest()

            assertEquals(Phase.REST, counter.value?.phase)
            finishCounter()
            assertNull(counter.value)
            assertEquals(action.value, Action.Stop)
        }
    }

    @Test
    fun `finishCounter when vibrationEnabled is true and phase is WORK`() {
        with(viewModel) {
            var localVibrationTimes = 0
            vibrationEnabled = true

            Mockito.`when`(customVibrator.vibrate(Mockito.anyInt()))
                .then {
                    localVibrationTimes = it.getArgument(0)
                    it
                }

            finishCounter()

            // Then vibration is executed just one time
            assertEquals(1, localVibrationTimes)
        }
    }

    @Test
    fun `finishCounter when vibrationEnabled is false`() {
        with(viewModel) {
            var wasCustomVibratorExecuted = false
            vibrationEnabled = false

            Mockito.`when`(customVibrator.vibrate())
                .then {
                    wasCustomVibratorExecuted = true
                    it
                }

            finishCounter()
            assertFalse(wasCustomVibratorExecuted)
        }
    }

    @Test
    fun `restartCounter when autoPlay is true`() {
        with(viewModel) {
            autoPlay = true

            restartCounter()
            assertEquals(action.value, Action.Play)
            assertNotNull(counter.value)
        }
    }

    @Test
    fun `restartCounter when autoPlay is false`() = runTest {
        with(viewModel) {
            Mockito.`when`(getPomodoroUseCase.invoke())
                .thenReturn(
                    Pomodoro(
                        workTime = 0L,
                        restTime = 0L
                    )
                )

            autoPlay = false
            restartCounter()

            assertEquals(0L, counter.value?.workTime)
            assertEquals(0L, counter.value?.restTime)
            assertEquals(action.value, Action.Stop)
        }
    }

    @Test
    fun `restartCounter when areSoundsEnable is true`() = runTest {
        with(viewModel) {
            var soundPlayed = false
            autoPlay = true
            areSoundsEnable = true

            Mockito.`when`(soundsManager.play())
                .then {
                    soundPlayed = true
                    it
                }

            restartCounter()
            assertTrue(soundPlayed)
        }
    }

    @Test
    fun `restartCounter when areSoundsEnable is false`() = runTest {
        with(viewModel) {
            var soundPlayed = false
            autoPlay = true
            areSoundsEnable = false

            Mockito.`when`(soundsManager.play())
                .then {
                    soundPlayed = true
                    it
                }

            restartCounter()
            assertFalse(soundPlayed)
        }
    }

    @Test
    fun `setTime when displayNotification is false`() = runTest {
        with(viewModel) {
            Mockito.`when`(getAutoPlayUseCase.invoke())
                .thenReturn(true)
            Mockito.`when`(isVibrationEnabledUseCase.invoke())
                .thenReturn(true)

            // Load counter value
            loadDefaultValues()

            setTime(10000)
            assertTrue(autoPlay)
            assertTrue(vibrationEnabled)
            assertEquals(10000L, counter.value?.countDown)
            assertFalse(displayNotification)
        }
    }

    @Test
    fun `closeNotification test`() {
        with(viewModel) {
            // Make sure notificationManager.close is called
            var isDisplayNotification = true
            Mockito.`when`(notificationManager.close())
                .then {
                    isDisplayNotification = false
                    null
                }

            closeNotification()
            assertFalse(displayNotification)
            assertFalse(isDisplayNotification)
        }
    }

    @Test
    fun `toggleNightMode changes liveData value`() = runTest {
        val first = viewModel.isNightMode.value
        viewModel.toggleNightMode()

        val second = viewModel.isNightMode.value
        viewModel.toggleNightMode()

        val third = viewModel.isNightMode.value

        assertTrue(first)
        assertFalse(second)
        assertTrue(third)
    }

    @Test
    fun `forceSelectedColorConfig test`() = runTest {
        val color = 10
        Mockito.`when`(getColorUseCase.invoke())
            .thenReturn(color)

        with(viewModel) {
            forceSelectedColorConfig()
            assertEquals(color, selectedColor.value)
        }
    }

    @Test
    fun `forceFetchKeepScreenConfig test`() = runTest {
        Mockito.`when`(isKeepScreenOnUseCase.invoke())
            .thenReturn(true)

        with(viewModel) {
            forceFetchKeepScreenConfig()
            assertTrue(keepScreenOn.getOrAwaitValue() ?: false)
        }
    }

    @Test
    fun `forceFetchVibrationConfig test`() = runTest {
        Mockito.`when`(isVibrationEnabledUseCase.invoke())
            .thenReturn(true)

        with(viewModel) {
            forceFetchVibrationConfig()
            assertTrue(vibrationEnabled)
        }
    }

    @Test
    fun `forceFetchSoundsConfig test`() = runTest {
        Mockito.`when`(areSoundsEnableUseCase.invoke())
            .thenReturn(false)

        with(viewModel) {
            forceFetchSoundsConfig()
            assertFalse(areSoundsEnable)
        }
    }

    @Test
    fun `openSettings changes liveData value`() {
        viewModel.openSettings()

        val result = viewModel.openSettings.getOrAwaitValue()
        assertTrue(result)
    }
}
