package com.emenjivar.pomodoro.ui.screens.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.pomodoro.usecases.GetColorUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val getColorUseCase: GetColorUseCase,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    testMode: Boolean = false
) : ViewModel() {

    private val _selectedColor = MutableLiveData<Int?>(null)
    val selectedColor: LiveData<Int?> = _selectedColor

    init {
        if (!testMode) {
            viewModelScope.launch(ioDispatcher) {
                loadSettings()
            }
        }
    }

    suspend fun loadSettings() {
        with(getColorUseCase.invoke()) {
            // Set default color in case of this
            // This prevent the app from getting stuck on splash screen
            //_selectedColor.postValue(this ?: ThemeColor.Tomato.color)
        }
    }
}
