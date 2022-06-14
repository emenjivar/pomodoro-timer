package com.emenjivar.pomodoro.screens.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.ui.theme.DefaultFont
import com.emenjivar.pomodoro.utils.toColor

@Composable
fun CustomDialog(
    defaultValue: String,
    title: String,
    subtitle: String,
    onSaveItem: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(defaultValue) }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = DefaultFont
                )
                Text(
                    text = subtitle,
                    fontFamily = DefaultFont,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = text,
                    onValueChange = {
                        if (it.isEmpty() || it.last().isDigit()) {
                            text = it
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = R.color.black.toColor(),
                        backgroundColor = R.color.white.toColor(),
                        focusedIndicatorColor = R.color.primary.toColor()
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CustomTextButton(
                        text = "CANCEL",
                        onClick = onDismiss
                    )

                    CustomTextButton(
                        text = "SAVE",
                        onClick = onSaveItem(text).run { onDismiss }
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomTextButton(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = Modifier.background(Color.Transparent),
        colors = ButtonDefaults.buttonColors(
            contentColor = R.color.primary.toColor(),
            backgroundColor = R.color.white.toColor()
        ),
        onClick = onClick
    ) {
        Text(text)
    }
}

@Preview(name = "Night mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CustomDialogPreview() {
    CustomDialog(
        defaultValue = "25:00",
        title = "Work time",
        subtitle = "Please enter the time in minutes",
        onSaveItem = {},
        onDismiss = {}
    )
}
