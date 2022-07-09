package com.emenjivar.pomodoro.screens.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InputTimeDialog(
    title: String,
    hours: String = "00",
    minutes: String = "00",
    seconds: String = "00",
    onInputChange: (Int) -> Unit,
    onBackSpace: () -> Unit,
    onDismiss: () -> Unit,
) {
    /*
    var text by remember { mutableStateOf(title) }

    Dialog(onDismissRequest = onDismiss) {
        InputTime(
            hours = hours,
            minutes = minutes,
            seconds = seconds,
            onInputChange = onInputChange,
            onBackSpace = onBackSpace
        )
    }
     */
}

@Composable
private fun CustomTextButton(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = Modifier.background(Color.Transparent),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colors.primaryVariant,
            backgroundColor = MaterialTheme.colors.surface
        ),
        onClick = onClick
    ) {
        Text(text)
    }
}

@Preview(name = "Night mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CustomDialogPreview() {
    /*
    CustomDialog(
        title = "",
        onDismiss = {},
        onInputChange = {},
        onBackSpace = {}
    )

     */
}
