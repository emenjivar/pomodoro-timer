package com.emenjivar.pomodoro.components.buttons

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme
import com.emenjivar.pomodoro.ui.theme.custom_button

@Composable
fun ButtonLowEmphasis(text: String, onClick: () -> Unit) {
    TextButton(
        modifier = Modifier.background(MaterialTheme.colors.background),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.custom_button
        )
    }
}

@Preview(showBackground = true, name = "Light mode")
@Composable
fun PreviewButtonLowEmphasis() {
    PomodoroSchedulerTheme {
        ButtonLowEmphasis(
            text = "CANCEL",
            onClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "Night mode")
@Composable
fun PreviewButtonLowEmphasisNight() {
    PomodoroSchedulerTheme {
        ButtonLowEmphasis(
            text = "CANCEL",
            onClick = {}
        )
    }
}
