package com.emenjivar.pomodoro.ui.screens.countdown

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.pomodoro.data.SettingsRepository
import com.emenjivar.pomodoro.usecases.AreSoundsEnableUseCase
import com.emenjivar.pomodoro.usecases.GetAutoPlayUseCase
import com.emenjivar.pomodoro.usecases.GetColorUseCase
import com.emenjivar.pomodoro.usecases.GetPomodoroUseCase
import com.emenjivar.pomodoro.usecases.IsKeepScreenOnUseCase
import com.emenjivar.pomodoro.usecases.IsNightModeUseCase
import com.emenjivar.pomodoro.usecases.IsVibrationEnabledUseCase
import com.emenjivar.pomodoro.usecases.SetNighModeUseCase
import com.emenjivar.pomodoro.data.SharedSettingsRepository
import com.emenjivar.pomodoro.utils.model.Counter
import com.emenjivar.pomodoro.utils.model.Phase
import com.emenjivar.pomodoro.system.CustomNotificationManager
import com.emenjivar.pomodoro.system.CustomVibrator
import com.emenjivar.pomodoro.system.SoundsManager
import com.emenjivar.pomodoro.utils.Action
import com.emenjivar.pomodoro.utils.ThemeColor
import com.emenjivar.pomodoro.utils.countDownInterval
import com.emenjivar.pomodoro.utils.toCounter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CountDownViewModel(
    sharedSettingsRepository: SharedSettingsRepository,
    settingsRepository: SettingsRepository,
    private val getColorUseCase: GetColorUseCase,
    private val getPomodoroUseCase: GetPomodoroUseCase,
    private val setNighModeUseCase: SetNighModeUseCase,
    private val getAutoPlayUseCase: GetAutoPlayUseCase,
    private val isNightModeUseCase: IsNightModeUseCase,
    private val isKeepScreenOnUseCase: IsKeepScreenOnUseCase,
    private val isVibrationEnabledUseCase: IsVibrationEnabledUseCase,
    private val areSoundsEnableUseCase: AreSoundsEnableUseCase,
    private val notificationManager: CustomNotificationManager,
    private val customVibrator: CustomVibrator,
    private val soundsManager: SoundsManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
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

    val uiState = CountDownUIState(
        colorTheme = colorTheme,
        counter = counter.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )
    )

    private var countDownTimer: CountDownTimer? = null

    private val _action: MutableLiveData<Action> = MutableLiveData(Action.Stop)
    val action: LiveData<Action> = _action

    private val _isNightMode = mutableStateOf(true)
    val isNightMode: State<Boolean> = _isNightMode

    private val _openSettings = MutableLiveData(false)
    val openSettings = _openSettings

    private val _keepScreenOn: MutableLiveData<Boolean?> = MutableLiveData()
    val keepScreenOn: LiveData<Boolean?> = _keepScreenOn

    private val _selectedColor = MutableLiveData<Int?>(null)
    val selectedColor: LiveData<Int?> = _selectedColor

    var autoPlay: Boolean = false
    var vibrationEnabled: Boolean = false
    var displayNotification: Boolean = false
    var areSoundsEnable: Boolean = true

    init {
        // Load set pomodoro
        settingsRepository.getPomodoro()
            .onEach { pomodoro ->
                counter.update { pomodoro.toCounter() }
                counterMemory = pomodoro.toCounter()
            }.launchIn(viewModelScope)

        if (!testMode) {
            viewModelScope.launch(ioDispatcher) {
                loadDefaultValues()
            }
        }
    }

    suspend fun loadDefaultValues() {
//        with(getColorUseCase.invoke()) {
//            _selectedColor.postValue(this ?: ThemeColor.Tomato.color)
//        }
//        _isNightMode.value = isNightModeUseCase.invoke()
        autoPlay = getAutoPlayUseCase.invoke()
        vibrationEnabled = isVibrationEnabledUseCase.invoke()

        // Set default pomodoro and load on livedata
//        _counter.value = fetchCounter()
        _keepScreenOn.postValue(isKeepScreenOnUseCase.invoke())
    }

    /**
     * Get pomodoro from dataStorage and convert to counter
     */
//    private suspend fun fetchCounter() = getPomodoroUseCase.invoke().toCounter()

    /**
     * In case of null, fetch local storage configurations
     */
//    suspend fun initCounter() {
//        if (_counter.value == null) {
//            _counter.value = fetchCounter()
//        }
//    }

    /**
     * recursive function
     * if _counter is null, fetch saved configuration and start the counter using workTime
     * if _counter is not null, stat the counter using restTime
     */
    fun startCounter() {
        viewModelScope.launch(Dispatchers.Main) {
//            initCounter()
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
        object : CountDownTimer(milliseconds, countDownInterval(milliseconds)) {
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
            counter.value?.let { safeCounter ->
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
            counter.update { counterMemory }
            countDownTimer?.cancel()
            notificationManager.close()
        }
    }

    /**
     * Update time only when counter is stopped
     * This is an easier way to update the value
     * instead on press stop button
     */
    fun updateCounterTime() {
        viewModelScope.launch(ioDispatcher) {
            if (action.value == Action.Stop) {
//                _counter.value = fetchCounter()
            }
        }
    }

    fun finishCounter() {
        var vibrationTimes = 1
        when (counter.value?.phase) {
            Phase.WORK -> {
//                _counter.value?.setRest()
            }
            Phase.REST -> {
                // This line force fetch pomodoro configurations and start from work
//                _counter.value = null
                _action.value = Action.Stop
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
    fun restartCounter() {
        finishCounter()

        if (counter.value != null || autoPlay) {
            if (areSoundsEnable) {
                soundsManager.play()
            }
            startCounter()
        } else {
            viewModelScope.launch(Dispatchers.Main) {
//                initCounter()
            }
        }
    }

    /**
     * Set current state of pomodoro on livedata value
     */
    fun setTime(milliseconds: Long) {
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
    fun forceSelectedColorConfig() = viewModelScope.launch(ioDispatcher) {
        _selectedColor.postValue(getColorUseCase.invoke())
    }

    fun forceFetchKeepScreenConfig() = viewModelScope.launch {
        _keepScreenOn.value = isKeepScreenOnUseCase.invoke()
    }

    fun forceFetchVibrationConfig() = viewModelScope.launch {
        vibrationEnabled = isVibrationEnabledUseCase.invoke()
    }

    fun forceFetchSoundsConfig() = viewModelScope.launch(ioDispatcher) {
    // TODO: put here sound configuration
    //        areSoundsEnable = areSoundsEnableUseCase.invoke()
    }

    fun forceAutoPlayConfig() = viewModelScope.launch(ioDispatcher) {
        autoPlay = getAutoPlayUseCase.invoke()
    }

    fun openSettings() {
        _openSettings.value = true
    }
}
