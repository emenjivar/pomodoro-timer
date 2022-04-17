package com.emenjivar.pomodoro.system

import com.emenjivar.pomodoro.model.Counter
import com.emenjivar.pomodoro.utils.Action

interface CustomNotificationManager {
    fun updateProgress(counter: Counter, action: Action?)
    fun close()
}
