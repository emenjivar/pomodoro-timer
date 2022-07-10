package com.emenjivar.pomodoro.ui.components.time

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.emenjivar.pomodoro.ui.components.buttons.ButtonLowEmphasis
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme
import com.emenjivar.pomodoro.ui.theme.subtitle

@Composable
fun InputTime(
    title: String = "Pomodoro time",
    hours: String,
    minutes: String,
    seconds: String,
    isPomodoro: Boolean,
    onInputChange: (digit: Int) -> Unit,
    onBackSpace: () -> Unit,
    onSaveLoadedTime: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.subtitle
                )

                InputTimeField(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                    onBackSpace = onBackSpace
                )

                Spacer(modifier = Modifier.height(8.dp))
                InputKeyboard(onInputChange = onInputChange)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ButtonLowEmphasis(
                        text = "CANCEL",
                        onClick = onDismiss
                    )
                    ButtonLowEmphasis(
                        text = "SAVE",
                        onClick = { onSaveLoadedTime(isPomodoro) }
                    )
                }
            }
        }
    }
}

@Preview(name = "Light mode")
@Composable
fun PreviewInputTimeModal() {
    PomodoroSchedulerTheme {
        InputTime(
            hours = "00",
            minutes = "24",
            seconds = "59",
            isPomodoro = true,
            onInputChange = {},
            onBackSpace = {},
            onSaveLoadedTime = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Dark mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewInputTimeModalDarkMode() {
    PomodoroSchedulerTheme {
        InputTime(
            hours = "00",
            minutes = "24",
            seconds = "59",
            isPomodoro = true,
            onInputChange = {},
            onBackSpace = {},
            onSaveLoadedTime = {},
            onDismiss = {}
        )
    }
}
