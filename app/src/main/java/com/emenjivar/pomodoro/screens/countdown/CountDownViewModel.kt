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
import com.emenjivar.core.usecase.GetPomodoroUseCase
import com.emenjivar.core.usecase.IsNightModeUseCase
import com.emenjivar.core.usecase.SetNighModeUseCase
import com.emenjivar.pomodoro.model.Counter
import com.emenjivar.pomodoro.model.Phase
import com.emenjivar.pomodoro.utils.Action
import com.emenjivar.pomodoro.utils.toCounter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountDownViewModel(
    private val getPomodoroUseCase: GetPomodoroUseCase,
    private val setNighModeUseCase: SetNighModeUseCase,
    private val isNightModeUseCase: IsNightModeUseCase,
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

    init {
        if (!testMode) {
            viewModelScope.launch(ioDispatcher) {
                loadDefaultValues()
            }
        }
    }

    suspend fun loadDefaultValues() {
        _isNightMode.value = isNightModeUseCase.invoke()

        // Set default pomodoro and load on livedata
        _counter.value = fetchCounter()
    }

    /**
     * Get pomodoro from dataStorage and convert to counter
     */
    private suspend fun fetchCounter() = getPomodoroUseCase.invoke().toCounter()

    fun finishCounter() {
        when (counter.value?.phase) {
            Phase.WORK -> {
                counter.value?.phase = Phase.REST
            }
            Phase.REST -> {
                counter.value?.setRest()
            }
        }
    }

    /**
     * recursive function
     * if _counter is null, fetch saved configuration and start the counter using workTime
     * if _counter is not null, stat the counter using restTime
     */
    fun startCounter() {
        viewModelScope.launch(Dispatchers.Main) {
            // In case of null, fetch local storage configurations
            if (_counter.value == null) {
                _counter.value = fetchCounter()
            }

            counter.value?.let { safeCounter ->
                val milliseconds = if (safeCounter.phase == Phase.WORK)
                    safeCounter.workTime
                else
                    safeCounter.restTime

                _action.value = Action.Play

                if (!testMode) {
                    // Don't include this block on testing
                    countDownTimer = object : CountDownTimer(milliseconds, 500) {
                        override fun onTick(millisUntilFinished: Long) {
                            setTime(millisUntilFinished)
                        }

                        override fun onFinish() {
                            finishCounter()
                            viewModelScope.launch {
                                startCounter()
                            }
                        }
                    }.start()
                }
            }
        }
    }

    fun pauseCounter() {
        _action.value = Action.Pause
        countDownTimer?.cancel()
    }

    fun stopCounter() {
        viewModelScope.launch(ioDispatcher) {
            _action.postValue(Action.Stop)
            _counter.value = fetchCounter()
            countDownTimer?.cancel()
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
    }

    fun toggleNightMode() {
        val nightMode = isNightMode.value.not()
        _isNightMode.value = nightMode

        viewModelScope.launch(ioDispatcher) {
            setNighModeUseCase.invoke(nightMode)
        }
    }

    fun openSettings() {
        _openSettings.value = true
    }
}
