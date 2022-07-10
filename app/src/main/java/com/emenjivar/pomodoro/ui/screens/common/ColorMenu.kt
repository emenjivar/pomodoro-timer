package com.emenjivar.pomodoro.ui.screens.common

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.utils.ThemeColor
import com.emenjivar.pomodoro.utils.toColor
import com.emenjivar.pomodoro.utils.toIcon

@Composable
private fun Square(
    theme: ThemeColor,
    selected: Boolean = false,
    onSelectTheme: (Int) -> Unit
) {
    // val check = remember { mutableStateOf(selected) }
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(60.dp)
            .background(theme.color.toColor())
            .clickable {
                // check.value = !check.value
                onSelectTheme(theme.color)
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = selected,
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
fun ColorMenu(
    selectedColor: Int? = null,
    onSelectTheme: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Square(
            theme = ThemeColor.Tomato,
            selected = selectedColor == null || selectedColor == ThemeColor.Tomato.color,
            onSelectTheme = onSelectTheme
        )
        Square(
            theme = ThemeColor.Orange,
            selected = selectedColor == ThemeColor.Orange.color,
            onSelectTheme = onSelectTheme
        )
        Square(
            theme = ThemeColor.Yellow,
            selected = selectedColor == ThemeColor.Yellow.color,
            onSelectTheme = onSelectTheme
        )
        Square(
            theme = ThemeColor.Green,
            selected = selectedColor == ThemeColor.Green.color,
            onSelectTheme = onSelectTheme
        )
        Square(
            theme = ThemeColor.LeafGreen,
            selected = selectedColor == ThemeColor.LeafGreen.color,
            onSelectTheme = onSelectTheme
        )
    }
}

@Preview(name = "default theme", showBackground = true)
@Composable
fun PreviewColorMenu() {
    ColorMenu {}
}

@Preview(name = "Orange theme", showBackground = true)
@Composable
fun PreviewColorMenuOrange() {
    ColorMenu(ThemeColor.Orange.color) {}
}

@Preview(name = "Yellow theme", showBackground = true)
@Composable
fun PreviewColorMenuYellow() {
    ColorMenu(ThemeColor.Orange.color) {}
}

@Preview(name = "Green theme", showBackground = true)
@Composable
fun PreviewColorMenuWine() {
    ColorMenu(ThemeColor.Green.color) {}
}

@Preview(name = "Leaf green theme", showBackground = true)
@Composable
fun PreviewColorMenuBasil() {
    ColorMenu(ThemeColor.LeafGreen.color) {}
}
