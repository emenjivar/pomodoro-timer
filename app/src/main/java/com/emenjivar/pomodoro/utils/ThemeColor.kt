package com.emenjivar.pomodoro.utils

import androidx.annotation.ColorRes
import com.emenjivar.pomodoro.R

sealed class ThemeColor(@ColorRes val color: Int) {
    class Tomato : ThemeColor(R.color.primary)
    class Orange : ThemeColor(R.color.orange)
    class Wine : ThemeColor(R.color.wine)
    class Basil : ThemeColor(R.color.basil)
    class Charcoal : ThemeColor(R.color.charcoal)
}
