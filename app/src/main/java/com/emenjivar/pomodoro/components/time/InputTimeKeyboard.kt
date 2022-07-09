package com.emenjivar.pomodoro.components.time

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme
import com.emenjivar.pomodoro.ui.theme.headline5

@Composable
fun InputKeyboard(
    onInputChange: (digit: Int) -> Unit
) {
    Column(modifier = Modifier.width(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            InputKeyboardButton(digit = 1, onPressKey = onInputChange)
            InputKeyboardButton(digit = 2, onPressKey = onInputChange)
            InputKeyboardButton(digit = 3, onPressKey = onInputChange)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            InputKeyboardButton(digit = 4, onPressKey = onInputChange)
            InputKeyboardButton(digit = 5, onPressKey = onInputChange)
            InputKeyboardButton(digit = 6, onPressKey = onInputChange)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            InputKeyboardButton(digit = 7, onPressKey = onInputChange)
            InputKeyboardButton(digit = 8, onPressKey = onInputChange)
            InputKeyboardButton(digit = 9, onPressKey = onInputChange)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            InputKeyboardButton(digit = 0, onPressKey = onInputChange)
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
        InputKeyboard(onInputChange = {})
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewInputKeyboardDarkMode() {
    PomodoroSchedulerTheme {
        InputKeyboard(onInputChange = {})
    }
}
