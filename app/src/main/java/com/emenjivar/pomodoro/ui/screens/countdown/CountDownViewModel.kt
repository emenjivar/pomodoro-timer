package com.emenjivar.pomodoro.ui.screens.countdown

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.pomodoro.data.SettingsRepository
import com.emenjivar.pomodoro.data.SharedSettingsRepository
import com.emenjivar.pomodoro.system.CustomNotificationManager
import com.emenjivar.pomodoro.system.CustomVibrator
import com.emenjivar.pomodoro.system.SoundsManager
import com.emenjivar.pomodoro.utils.Action
import com.emenjivar.pomodoro.utils.ThemeColor
import com.emenjivar.pomodoro.utils.countDownInterval
import com.emenjivar.pomodoro.utils.model.Counter
import com.emenjivar.pomodoro.utils.model.Phase
import com.emenjivar.pomodoro.utils.toCounter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CountDownViewModel(
    sharedSettingsRepository: SharedSettingsRepository,
    settingsRepository: SettingsRepository,
    private val notificationManager: CustomNotificationManager,
    private val customVibrator: CustomVibrator,
    private val soundsManager: SoundsManager,
    private val testMode: Boolean = false
) : ViewModel() {

    private val colorTheme = sharedSettingsRepository.getColorTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ThemeColor.Tomato.color
        )

    private var counterMemory: Counter? = null
    private val counter = MutableStateFlow<Counter?>(null)

    private var countDownTimer: CountDownTimer? = null

    private val action = MutableStateFlow<Action>(Action.Stop)

    var autoPlay: Boolean = false
    var vibrationEnabled: Boolean = false
    var displayNotification: Boolean = false
    var areSoundsEnable: Boolean = true

    init {
        settingsRepository.getPomodoro()
            .onEach { pomodoro ->
                counter.update { pomodoro.toCounter() }
                counterMemory = pomodoro.toCounter()
            }.launchIn(
                scope = viewModelScope
            )
    }

    /**
     * recursive function
     * if _counter is null, fetch saved configuration and start the counter using workTime
     * if _counter is not null, stat the counter using restTime
     */
    private fun onPlay() {
        counter.value?.let { safeCounter ->
            val milliseconds = if (safeCounter.phase == Phase.WORK)
                safeCounter.workTime
            else
                safeCounter.restTime

            action.update { Action.Play }

            if (!testMode) {
                // Don't include this block on testing
                countDownTimer = countDownTimer(milliseconds).start()
            }
        }
    }

    private fun countDownTimer(milliseconds: Long) =
        object : CountDownTimer(milliseconds, countDownInterval(milliseconds)) {
            override fun onTick(millisUntilFinished: Long) {
                setTime(millisUntilFinished)
            }

            override fun onFinish() {
                restartCounter()
            }
        }

    private fun onPause() {
        action.update { Action.Pause }
        countDownTimer?.cancel()

        if (displayNotification) {
            updateNotification(Action.Pause)
        }
    }

    private fun onResume() {
        action.update { Action.Resume }

        // Don't include this block on tests
        if (!testMode) {
            counter.value?.let { safeCounter ->
                countDownTimer = countDownTimer(safeCounter.countDown).start()
            }
        }

        if (displayNotification) {
            updateNotification(Action.Play)
        }
    }

    private fun onStop() {
        action.update { Action.Stop }
        counter.update { counterMemory }
        countDownTimer?.cancel()
        notificationManager.close()
    }

    private fun finishCounter() {
        var vibrationTimes = 1
        when (counter.value?.phase) {
            Phase.WORK -> {
//                _counter.value?.setRest()
                counter.value?.setRest()
            }
            Phase.REST -> {
                // This line force fetch pomodoro configurations and start from work
                counter.update { null }
                action.update { Action.Stop }
                vibrationTimes = 2
            }
            else -> {
                // Nothing to do here
            }
        }

        if (vibrationEnabled) {
            // Vibrate 2 times when complete pomodoro is finished
            customVibrator.vibrate(vibrationTimes)
        }
    }

    /**
     * Restart countdown and start the counter when autoPlay flag is true
     * else, just init the counter on Stop using default values
     */
    private fun restartCounter() {
        finishCounter()

        if (counter.value != null || autoPlay) {
            if (areSoundsEnable) {
                soundsManager.play()
            }
            onPlay()
        } else {
            viewModelScope.launch(Dispatchers.Main) {
//                initCounter()
            }
        }
    }

    /**
     * Set current state of pomodoro on livedata value
     */
    private fun setTime(milliseconds: Long) {
        counter.update {
            it?.copy(countDown = milliseconds)
        }

        /**
         * displayNotification is set during activity lifecycle
         * display during onStop action
         */
        if (displayNotification && counter.value != null) {
//            notificationManager.updateProgress(
//                counter = counter.value,
//                action = Action.Play
//            )
        }
    }

    private fun updateNotification(action: Action) {
//        _counter.value?.let { safeCounter ->
//            notificationManager.updateProgress(
//                counter = safeCounter,
//                action = action
//            )
//        }
    }

    private fun closeNotification() {
        displayNotification = false
        notificationManager.close()
    }

    val uiState = CountDownUIState(
        colorTheme = colorTheme,
        counter = counter.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        ),
        action = action,
        onPlay = ::onPlay,
        onPause = ::onPause,
        onResume = ::onResume,
        onStop = ::onStop
    )
}
