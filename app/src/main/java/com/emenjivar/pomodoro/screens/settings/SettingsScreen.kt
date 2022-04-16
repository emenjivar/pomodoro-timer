package com.emenjivar.pomodoro.screens.settings

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.emenjivar.pomodoro.screens.common.CustomDialog

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val pomodoroTime by viewModel.pomodoroTime.observeAsState(0L)
    val restTime by viewModel.restTime.observeAsState(0L)
    val autoPlay by viewModel.autoPlay

    SettingsScreen(
        pomodoroTime = pomodoroTime,
        restTime = restTime,
        autoPlay = autoPlay,
        backAction = { viewModel.closeSettings() },
        setPomodoroTime = { viewModel.setPomodoroTime(it) },
        setRestTime = {
            viewModel.setRestTime(it)
            Log.d("SettingsScreen", "onSaveItem...")
        },
        onCheckedChange = {
            viewModel.setAutoPlay(it)
        }
    )
}

@Composable
fun SettingsScreen(
    pomodoroTime: Long,
    restTime: Long,
    autoPlay: Boolean,
    backAction: () -> Unit,
    setPomodoroTime: (time: String) -> Unit,
    setRestTime: (time: String) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings"
                    )
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
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            SettingsGroup(title = "Time settings") {
                SettingsItem(
                    defaultValue = pomodoroTime.toString(),
                    title = "Pomodoro",
                    description = "Defines the duration of the intervals",
                    titleDialog = "Pomodoro time",
                    descriptionDialog = "Please enter the duration in minutes",
                    onSaveItem = setPomodoroTime
                ) {
                    SettingsRightText("$pomodoroTime min.")
                }
                SettingsItem(
                    defaultValue = restTime.toString(),
                    title = "Rest",
                    description = "Defines the duration of the intervals after Pomodoros",
                    titleDialog = "Rest time",
                    descriptionDialog = "Please enter the duration in minutes",
                    onSaveItem = setRestTime
                ) {
                    SettingsRightText("$restTime min.")
                }
            }

            SettingsGroup(title = "Others") {
                SwitchItem(
                    title = "Keep screen on",
                    subtitle = "At the end of a pomodoro, the next one start automatically",
                    autoPlay = autoPlay,
                    onCheckedChange = onCheckedChange
                )
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
fun SwitchItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String?,
    autoPlay: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!autoPlay) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(2f)
        ) {
            Text(title)

            subtitle?.let { safeSubtitle ->
                Text(
                    text = safeSubtitle,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(
                        top = 8.dp
                    )
                )
            }
        }

        Column(
            modifier = Modifier.weight(0.8f),
            horizontalAlignment = Alignment.End
        ) {
            Switch(
                checked = autoPlay,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    defaultValue: String,
    title: String,
    description: String? = null,
    titleDialog: String,
    descriptionDialog: String,
    onSaveItem: (String) -> Unit,
    action: @Composable () -> Unit
) {
    val showCustomDialog = remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showCustomDialog.value = !showCustomDialog.value }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(2f)
        ) {
            Text(
                text = title
            )
            description?.let { safeDescription ->
                Text(
                    text = safeDescription,
                    fontSize = 15.sp,
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

    if (showCustomDialog.value) {
        CustomDialog(
            defaultValue = defaultValue,
            title = titleDialog,
            subtitle = descriptionDialog,
            onSaveItem = onSaveItem,
            onDismiss = {
                showCustomDialog.value = false
                Log.d("SettingsScreen", "onDismiss...")
            }
        )
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
            pomodoroTime = 0L,
            restTime = 0L,
            autoPlay = false,
            backAction = {},
            setPomodoroTime = {},
            setRestTime = {},
            onCheckedChange = {}
        )
    }
}
