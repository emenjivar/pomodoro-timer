package com.emenjivar.pomodoro.utils

sealed class Action {
    object Play : Action()
    object Pause : Action()
    object Resume : Action()
    object Stop : Action()
}
