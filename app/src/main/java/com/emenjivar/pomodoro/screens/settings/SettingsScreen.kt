package com.emenjivar.pomodoro.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emenjivar.pomodoro.R

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    SettingsScreen(
        backAction = { viewModel.closeSettings() }
    )
}

@Composable
fun SettingsScreen(
    backAction: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                },
                navigationIcon = {
                    IconButton(onClick = backAction) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_24),
                            contentDescription = null
                        )
                    }
                },
                backgroundColor = colorResource(R.color.primary),
                contentColor = colorResource(R.color.white)
            )
        }
    ) {
        Column {
            SettingsGroup(title = "Time settings") {
                SettingsItem(
                    title = "Pomodoro",
                    description = "Defines the duration of the intervals"
                ) {
                    SettingsRightText("25 min.")
                }
                SettingsItem(
                    title = "Rest",
                    description = "Defines the duration of the intervals after Pomodoros"
                ) {
                    SettingsRightText("5 min.")
                }
            }

            SettingsGroup(title = "Sounds") {
                SettingsItem(
                    title = "Pomodoro",
                    description = "Select the sound to notify the end of the pomodoro"
                ) {
                    SettingsRightText("Bell")
                }
                SettingsItem(
                    title = "Rest",
                    description = "Select the sound to notify the end of the block of time"
                ) {
                    SettingsRightText("Drop")
                }
            }

            SettingsGroup(title = "Others") {
                SettingsItem(
                    title = "Keep screen on"
                ) { }
            }
        }
    }
}

@Composable
fun SettingsGroup(
    title: String,
    action: @Composable () -> Unit
) {
    Text(
        text = title,
        color = colorResource(id = R.color.primary),
        fontSize = 13.sp,
        modifier = Modifier.padding(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 8.dp
        )
    )
    action()
}

@Composable
fun SettingsItem(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    action: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(2f)
        ) {
            Text(
                text = title,
                fontFamily = FontFamily(Font(R.font.ubuntu_regular))
            )
            description?.let { safeDescription ->
                Text(
                    text = safeDescription,
                    fontFamily = FontFamily(Font(R.font.ubuntu_regular)),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(
                        top = 8.dp
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(0.8f),
            horizontalAlignment = Alignment.End
        ) {
            action()
        }
    }
}

@Composable
fun SettingsRightText(text: String) {
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.ubuntu_regular))
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewStingsItem() {
    MaterialTheme {
        SettingsScreen(
            backAction = {}
        )
    }
}
