package com.emenjivar.pomodoro.utils

import androidx.annotation.ColorRes
import com.emenjivar.pomodoro.R

sealed class ThemeColor(@ColorRes val color: Int) {
    object Tomato : ThemeColor(R.color.primary)
    object Orange : ThemeColor(R.color.orange)
    object Yellow : ThemeColor(R.color.yellow)
    object Green : ThemeColor(R.color.green)
    object LeafGreen : ThemeColor(R.color.leaf_green)
}
