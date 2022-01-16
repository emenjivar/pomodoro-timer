package com.emenjivar.pomodoro.screens.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val _closeSettings = MutableLiveData(false)
    val closeSettings = _closeSettings

    fun closeSettings() {
        _closeSettings.value = true
    }
}
