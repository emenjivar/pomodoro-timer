package com.emenjivar.pomodoro.utils

sealed class Action {
    object Play : Action()
    object Pause : Action()
    object Stop : Action()
    object Rest : Action()
}
