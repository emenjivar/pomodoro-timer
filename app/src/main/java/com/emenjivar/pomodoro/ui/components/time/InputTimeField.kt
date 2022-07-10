package com.emenjivar.pomodoro.ui.components.time

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme
import com.emenjivar.pomodoro.ui.theme.headline5
import com.emenjivar.pomodoro.ui.theme.subtitle

@Composable
fun InputTimeField(
    hours: String = "00",
    minutes: String = "00",
    seconds: String = "00",
    onBackSpace: () -> Unit,
) {
    Column(
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TimeSplit(time = hours, unit = "h")
            TimeSplit(time = minutes, unit = "m")
            TimeSplit(time = seconds, unit = "s")

            IconButton(onClick = onBackSpace) {
                Icon(
                    tint = MaterialTheme.colors.subtitle,
                    painter = painterResource(id = R.drawable.ic_baseline_backspace_24),
                    contentDescription = "Backspace"
                )
            }
        }
        Divider(thickness = 2.dp, color = MaterialTheme.colors.subtitle)
    }
}

@Composable
fun TimeSplit(time: String = "00", unit: String) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = time,
            style = MaterialTheme.typography.headline5,
            color = MaterialTheme.colors.subtitle
        )
        Text(
            text = unit,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.subtitle
        )
        Spacer(modifier = Modifier.width(3.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFormattedTime() {
    PomodoroSchedulerTheme {
        InputTimeField(
            hours = "00",
            minutes = "25",
            seconds = "00",
            onBackSpace = {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewFormattedTimeDarkMode() {
    PomodoroSchedulerTheme {
        InputTimeField(
            hours = "00",
            minutes = "25",
            seconds = "00",
            onBackSpace = {}
        )
    }
}
