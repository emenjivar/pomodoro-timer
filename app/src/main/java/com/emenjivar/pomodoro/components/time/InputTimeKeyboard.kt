package com.emenjivar.pomodoro.components.time

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme
import com.emenjivar.pomodoro.ui.theme.headline5

@Composable
fun InputKeyboard(
    onPressKey: (digit: Int) -> Unit
) {
    Column(modifier = Modifier.width(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            InputKeyboardButton(digit = 1, onPressKey = onPressKey)
            InputKeyboardButton(digit = 2, onPressKey = onPressKey)
            InputKeyboardButton(digit = 3, onPressKey = onPressKey)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            InputKeyboardButton(digit = 4, onPressKey = onPressKey)
            InputKeyboardButton(digit = 5, onPressKey = onPressKey)
            InputKeyboardButton(digit = 6, onPressKey = onPressKey)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            InputKeyboardButton(digit = 7, onPressKey = onPressKey)
            InputKeyboardButton(digit = 8, onPressKey = onPressKey)
            InputKeyboardButton(digit = 7, onPressKey = onPressKey)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            InputKeyboardButton(digit = 0, onPressKey = onPressKey)
        }
    }
}

@Composable
fun InputKeyboardButton(
    digit: Int,
    onPressKey: (digit: Int) -> Unit
) {
    OutlinedButton(
        modifier = Modifier
            .size(80.dp),
        border = BorderStroke(0.dp, MaterialTheme.colors.background),
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = MaterialTheme.colors.background
        ),
        onClick = { onPressKey(digit) }
    ) {
        Text(
            text = digit.toString(),
            style = MaterialTheme.typography.headline5
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInputKeyboard() {
    PomodoroSchedulerTheme {
        InputKeyboard(
            onPressKey = {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewInputKeyboardDarkMode() {
    PomodoroSchedulerTheme {
        InputKeyboard(
            onPressKey = {}
        )
    }
}