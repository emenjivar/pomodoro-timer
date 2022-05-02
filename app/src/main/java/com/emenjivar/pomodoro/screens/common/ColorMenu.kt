package com.emenjivar.pomodoro.screens.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.utils.ThemeColor
import com.emenjivar.pomodoro.utils.toColor
import com.emenjivar.pomodoro.utils.toIcon

@Composable
private fun Square(theme: ThemeColor, selected: Boolean = false) {
    val check = remember { mutableStateOf(selected) }
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(60.dp)
            .background(theme.color.toColor())
            .clickable { check.value = !check.value },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = check.value,
            enter = fadeIn(initialAlpha = 0.3f),
            exit = fadeOut()
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = R.drawable.ic_baseline_check_24.toIcon(),
                tint = R.color.white.toColor(),
                contentDescription = null
            )
        }
    }
}

@Composable
fun ColorMenu() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Square(
            theme = ThemeColor.Tomato(),
            selected = true
        )

        Square(theme = ThemeColor.Orange())
        Square(theme = ThemeColor.Wine())
        Square(theme = ThemeColor.Basil())
        Square(theme = ThemeColor.Charcoal())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewColorMenu() {
    ColorMenu()
}
