package com.emenjivar.pomodoro.screens.countdown

import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.usecase.GetAutoPlayUseCase
import com.emenjivar.core.usecase.GetPomodoroUseCase
import com.emenjivar.core.usecase.IsKeepScreenOnUseCase
import com.emenjivar.core.usecase.IsNightModeUseCase
import com.emenjivar.core.usecase.IsVibrationEnabledUseCase
import com.emenjivar.core.usecase.SetNighModeUseCase
import com.emenjivar.pomodoro.model.Counter
import com.emenjivar.pomodoro.model.Phase
import com.emenjivar.pomodoro.system.CustomNotificationManager
import com.emenjivar.pomodoro.system.CustomVibrator
import com.emenjivar.pomodoro.utils.Action
import com.emenjivar.pomodoro.utils.toCounter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountDownViewModel(
    private val getPomodoroUseCase: GetPomodoroUseCase,
    private val setNighModeUseCase: SetNighModeUseCase,
    private val getAutoPlayUseCase: GetAutoPlayUseCase,
    private val isNightModeUseCase: IsNightModeUseCase,
    private val isKeepScreenOnUseCase: IsKeepScreenOnUseCase,
    private val isVibrationEnabledUseCase: IsVibrationEnabledUseCase,
    private val notificationManager: CustomNotificationManager,
    private val customVibrator: CustomVibrator,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val testMode: Boolean = false
) : ViewModel() {

    private var countDownTimer: CountDownTimer? = null

    /**
     * Complex object, observed only in compose UI
     * define neverEqualPolicy to observe nested property changes
     */
    private val _counter: MutableState<Counter?> =
        mutableStateOf(value = null, policy = neverEqualPolicy())
    val counter: State<Counter?> = _counter

    private val _action: MutableLiveData<Action> = MutableLiveData(Action.Stop)
    val action: LiveData<Action> = _action

    private val _isNightMode = mutableStateOf(true)
    val isNightMode: State<Boolean> = _isNightMode

    private val _openSettings = MutableLiveData(false)
    val openSettings = _openSettings

    private val _keepScreenOn: MutableLiveData<Boolean?> = MutableLiveData()
    val keepScreenOn: LiveData<Boolean?> = _keepScreenOn

    var autoPlay: Boolean = false
    var vibrationEnabled: Boolean = false
    var displayNotification: Boolean = false

    init {
        if (!testMode) {
            viewModelScope.launch(ioDispatcher) {
                loadDefaultValues()
            }
        }
    }

    suspend fun loadDefaultValues() {
        _isNightMode.value = isNightModeUseCase.invoke()
        autoPlay = getAutoPlayUseCase.invoke()
        vibrationEnabled = isVibrationEnabledUseCase.invoke()

        // Set default pomodoro and load on livedata
        _counter.value = fetchCounter()
        _keepScreenOn.postValue(isKeepScreenOnUseCase.invoke())
    }

    /**
     * Get pomodoro from dataStorage and convert to counter
     */
    private suspend fun fetchCounter() = getPomodoroUseCase.invoke().toCounter()

    /**
     * In case of null, fetch local storage configurations
     */
    suspend fun initCounter() {
        if (_counter.value == null) {
            _counter.value = fetchCounter()
        }
    }

    /**
     * recursive function
     * if _counter is null, fetch saved configuration and start the counter using workTime
     * if _counter is not null, stat the counter using restTime
     */
    fun startCounter() {
        viewModelScope.launch(Dispatchers.Main) {
            initCounter()
            counter.value?.let { safeCounter ->
                val milliseconds = if (safeCounter.phase == Phase.WORK)
                    safeCounter.workTime
                else
                    safeCounter.restTime

                _action.value = Action.Play

                if (!testMode) {
                    // Don't include this block on testing
                    countDownTimer = countDownTimer(milliseconds).start()
                }
            }
        }
    }

    private fun countDownTimer(milliseconds: Long) =
        object : CountDownTimer(milliseconds, 500) {
            override fun onTick(millisUntilFinished: Long) {
                setTime(millisUntilFinished)
            }

            override fun onFinish() {
                restartCounter()
            }
        }

    fun pauseCounter() {
        _action.value = Action.Pause
        countDownTimer?.cancel()

        if (displayNotification) {
            updateNotification(Action.Pause)
        }
    }

    fun resumeCounter() {
        _action.value = Action.Resume

        // Don't include this block on tests
        if (!testMode) {
            _counter.value?.let { safeCounter ->
                countDownTimer = countDownTimer(safeCounter.countDown).start()
            }
        }

        if (displayNotification) {
            updateNotification(Action.Play)
        }
    }

    fun stopCounter() {
        viewModelScope.launch(ioDispatcher) {
            _action.postValue(Action.Stop)
            _counter.value = fetchCounter()
            countDownTimer?.cancel()
            notificationManager.close()
        }
    }

    fun finishCounter() {
        var vibrationTimes = 1
        when (counter.value?.phase) {
            Phase.WORK -> {
                _counter.value?.setRest()
            }
            Phase.REST -> {
                // This line force fetch pomodoro configurations and start from work
                _counter.value = null
                _action.value = Action.Stop
                vibrationTimes = 2
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
    fun restartCounter() {
        finishCounter()

        if (counter.value != null || autoPlay) {
            startCounter()
        } else {
            viewModelScope.launch(Dispatchers.Main) {
                initCounter()
            }
        }
    }

    /**
     * Set current state of pomodoro on livedata value
     */
    fun setTime(milliseconds: Long) {
        val localCounter = counter.value?.apply {
            countDown = milliseconds
        }
        _counter.value = localCounter

        /**
         * displayNotification is set during activity lifecycle
         * display during onStop action
         */
        if (displayNotification && localCounter != null) {
            notificationManager.updateProgress(
                counter = localCounter,
                action = Action.Play
            )
        }
    }

    private fun updateNotification(action: Action) {
        _counter.value?.let { safeCounter ->
            notificationManager.updateProgress(
                counter = safeCounter,
                action = action
            )
        }
    }

    fun closeNotification() {
        displayNotification = false
        notificationManager.close()
    }

    fun toggleNightMode() {
        val nightMode = isNightMode.value.not()
        _isNightMode.value = nightMode

        viewModelScope.launch(ioDispatcher) {
            setNighModeUseCase.invoke(nightMode)
        }
    }

    /**
     * Fetch local storage configurations
     */
    fun forceFetchKeepScreenConfig() = viewModelScope.launch {
        _keepScreenOn.value = isKeepScreenOnUseCase.invoke()
    }

    fun forceFetchVibrationConfig() = viewModelScope.launch {
        vibrationEnabled = isVibrationEnabledUseCase.invoke()
    }

    fun openSettings() {
        _openSettings.value = true
    }
}
