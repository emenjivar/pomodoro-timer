package com.emenjivar.pomodoro.utils

import androidx.annotation.ColorRes
import com.emenjivar.pomodoro.R

sealed class ThemeColor(@ColorRes val color: Int) {
    object Tomato : ThemeColor(R.color.primary)
    object Orange : ThemeColor(R.color.orange)
    object Wine : ThemeColor(R.color.wine)
    object Basil : ThemeColor(R.color.basil)
    object Charcoal : ThemeColor(R.color.charcoal)
}
