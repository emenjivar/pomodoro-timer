package com.emenjivar.pomodoro.components.time

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emenjivar.pomodoro.components.buttons.ButtonLowEmphasis
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme

@Composable
fun InputTime() {
    Card(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Pomodoro time")
            InputTimeField(hours = "00", minutes = "00", seconds = "00") {

            }

            Spacer(modifier = Modifier.height(8.dp))
            InputKeyboard(onPressKey = { Log.d("TimeSettings", "Digit pressed: $it") })

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonLowEmphasis(
                    text = "CANCEL",
                    onClick = {}
                )
                ButtonLowEmphasis(
                    text = "SAVE",
                    onClick = {}
                )
            }
        }

    }
}

@Preview(name = "Light mode")
@Composable
fun PreviewInputTimeModal() {
    PomodoroSchedulerTheme {
        InputTime()
    }
}

@Preview(name = "Dark mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewInputTimeModalDarkMode() {
    PomodoroSchedulerTheme {
        InputTime()
    }
}
