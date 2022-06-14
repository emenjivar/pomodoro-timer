package com.emenjivar.pomodoro.screens.settings

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.screens.common.ColorMenu
import com.emenjivar.pomodoro.screens.common.CustomDialog
import com.emenjivar.pomodoro.utils.TRANSITION_DURATION
import com.emenjivar.pomodoro.utils.ThemeColor
import com.emenjivar.pomodoro.utils.toColor

/**
 * @param selectedColor reduce ui color delay, loading this value
 *  while viewModel finish to fetch the color
 */
@Composable
fun SettingsScreen(
    selectedColor: Int? = null,
    viewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val viewModelSelectedColor by viewModel.selectedColor.observeAsState()
    val themeColor = with(viewModelSelectedColor ?: selectedColor) {
        if (this != 0) this
        else R.color.primary
    }
    val pomodoroTime by viewModel.pomodoroTime.observeAsState(0L)
    val restTime by viewModel.restTime.observeAsState(0L)
    val autoPlay by viewModel.autoPlay
    val keepScreenOn by viewModel.keepScreenOn
    val vibrationEnabled by viewModel.vibrationEnabled
    val soundsEnable by viewModel.soundsEnable

    SettingsScreen(
        selectedColor = themeColor,
        pomodoroTime = pomodoroTime,
        restTime = restTime,
        autoPlay = autoPlay,
        keepScreenOn = keepScreenOn,
        vibrationEnabled = vibrationEnabled,
        soundsEnable = soundsEnable,
        backAction = { viewModel.closeSettings() },
        onSelectTheme = { viewModel.setColor(it) },
        setPomodoroTime = { viewModel.setPomodoroTime(it) },
        setRestTime = {
            viewModel.setRestTime(it)
        },
        onAutoPlayChange = {
            viewModel.setAutoPlay(it)
        },
        onKeepScreenChange = {
            viewModel.setKeepScreenOn(it)
        },
        onVibrationEnabledChange = {
            viewModel.setVibration(it)
        },
        onSoundsEnableChange = {
            viewModel.setSoundsEnable(it)
        }
    )
}

@Composable
fun SettingsScreen(
    selectedColor: Int? = null,
    pomodoroTime: Long,
    restTime: Long,
    autoPlay: Boolean,
    keepScreenOn: Boolean,
    vibrationEnabled: Boolean,
    soundsEnable: Boolean,
    backAction: () -> Unit,
    onSelectTheme: (Int) -> Unit,
    setPomodoroTime: (time: String) -> Unit,
    setRestTime: (time: String) -> Unit,
    onAutoPlayChange: (Boolean) -> Unit,
    onKeepScreenChange: (Boolean) -> Unit,
    onVibrationEnabledChange: (Boolean) -> Unit,
    onSoundsEnableChange: (Boolean) -> Unit
) {
    val topAppBarColor = animateColorAsState(
        targetValue = selectedColor?.toColor() ?: ThemeColor.Tomato.color.toColor(),
        animationSpec = tween(durationMillis = TRANSITION_DURATION)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Settings")
                },
                navigationIcon = {
                    IconButton(onClick = backAction) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_24),
                            contentDescription = null
                        )
                    }
                },
                backgroundColor = topAppBarColor.value,
                contentColor = colorResource(R.color.white)
            )
        }
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .background(MaterialTheme.colors.background)
        ) {
            AppearanceSettings(
                selectedColor = selectedColor ?: ThemeColor.Tomato.color,
                onSelectTheme = onSelectTheme
            )

            TimeSettings(
                workTime = pomodoroTime,
                restTime = restTime,
                setPomodoroTime = setPomodoroTime,
                setRestTime = setRestTime
            )

            SoundSettings(
                selectedColor = selectedColor ?: ThemeColor.Tomato.color,
                isVibrationEnabled = vibrationEnabled,
                soundsEnable = soundsEnable,
                onVibrationEnabledChange = onVibrationEnabledChange,
                onSoundsEnableChange = onSoundsEnableChange
            )

            OthersSettings(
                selectedColor = selectedColor ?: ThemeColor.Tomato.color,
                autoPlay = autoPlay,
                keepScreenOn = keepScreenOn,
                onAutoPlayChange = onAutoPlayChange,
                onKeepScreenChange = onKeepScreenChange
            )
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
    subtitle: String? = null,
    value: Boolean,
    selectedColor: Int,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!value) }
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
                checked = value,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(selectedColor),
                    uncheckedThumbColor = colorResource(R.color.light_gray)
                )
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

@Composable
private fun AppearanceSettings(
    selectedColor: Int?,
    onSelectTheme: (Int) -> Unit
) = SettingsGroup(title = "Appearance settings") {
    Row(modifier = Modifier.padding(16.dp)) {
        ColorMenu(
            selectedColor = selectedColor,
            onSelectTheme = onSelectTheme
        )
    }
}

@Composable
private fun TimeSettings(
    workTime: Long,
    restTime: Long,
    setPomodoroTime: (time: String) -> Unit,
    setRestTime: (time: String) -> Unit
) = SettingsGroup(title = "Time settings") {
    SettingsItem(
        defaultValue = workTime.toString(),
        title = "Work",
        description = "Defines the duration of the intervals",
        titleDialog = "Work time",
        descriptionDialog = "Please enter the duration in minutes",
        onSaveItem = setPomodoroTime
    ) {
        SettingsRightText(text = "$workTime min.")
    }

    SettingsItem(
        defaultValue = restTime.toString(),
        title = "Rest",
        description = "Defines the duration of the rest intervals",
        titleDialog = "Rest time",
        descriptionDialog = "Please enter the duration in minutes",
        onSaveItem = setRestTime
    ) {
        SettingsRightText("$restTime min.")
    }
}

@Composable
private fun SoundSettings(
    selectedColor: Int,
    isVibrationEnabled: Boolean,
    soundsEnable: Boolean,
    onVibrationEnabledChange: (Boolean) -> Unit,
    onSoundsEnableChange: (Boolean) -> Unit
) = SettingsGroup(title = "Sounds") {
    SwitchItem(
        title = "Tune",
        subtitle = "Play a small sound when after every interval on time",
        value = soundsEnable,
        selectedColor = selectedColor,
        onCheckedChange = onSoundsEnableChange
    )
    SwitchItem(
        title = "Vibration",
        value = isVibrationEnabled,
        selectedColor = selectedColor,
        onCheckedChange = onVibrationEnabledChange
    )
}

@Composable
private fun OthersSettings(
    selectedColor: Int,
    autoPlay: Boolean,
    keepScreenOn: Boolean,
    onAutoPlayChange: (Boolean) -> Unit,
    onKeepScreenChange: (Boolean) -> Unit,
) = SettingsGroup(title = "Others") {
    SwitchItem(
        title = "Autoplay",
        subtitle = "At the end of a pomodoro, the next one start automatically",
        value = autoPlay,
        selectedColor = selectedColor,
        onCheckedChange = onAutoPlayChange
    )
    SwitchItem(
        title = "Keep screen on",
        value = keepScreenOn,
        selectedColor = selectedColor,
        onCheckedChange = onKeepScreenChange
    )
}

@Preview(
    name = "Normal settings",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewSettingsItem() {
    MaterialTheme {
        SettingsScreen(
            pomodoroTime = 0L,
            restTime = 0L,
            autoPlay = true,
            keepScreenOn = true,
            vibrationEnabled = true,
            soundsEnable = true,
            backAction = {},
            onSelectTheme = {},
            setPomodoroTime = {},
            setRestTime = {},
            onAutoPlayChange = {},
            onKeepScreenChange = {},
            onVibrationEnabledChange = {},
            onSoundsEnableChange = {}
        )
    }
}

@Preview(name = "Settings screen orange theme", showBackground = true)
@Composable
fun PreviewSettingsItemOrange() {
    MaterialTheme {
        SettingsScreen(
            selectedColor = ThemeColor.Orange.color,
            pomodoroTime = 0L,
            restTime = 0L,
            autoPlay = true,
            keepScreenOn = true,
            vibrationEnabled = true,
            soundsEnable = true,
            backAction = {},
            onSelectTheme = {},
            setPomodoroTime = {},
            setRestTime = {},
            onAutoPlayChange = {},
            onKeepScreenChange = {},
            onVibrationEnabledChange = {},
            onSoundsEnableChange = {}
        )
    }
}

@Preview(name = "Settings screen green theme", showBackground = true)
@Composable
fun PreviewSettingsItemWine() {
    MaterialTheme {
        SettingsScreen(
            selectedColor = ThemeColor.Green.color,
            pomodoroTime = 0L,
            restTime = 0L,
            autoPlay = true,
            keepScreenOn = true,
            vibrationEnabled = true,
            soundsEnable = true,
            backAction = {},
            onSelectTheme = {},
            setPomodoroTime = {},
            setRestTime = {},
            onAutoPlayChange = {},
            onKeepScreenChange = {},
            onVibrationEnabledChange = {},
            onSoundsEnableChange = {}
        )
    }
}

@Preview(name = "Settings screen leaf green theme", showBackground = true)
@Composable
fun PreviewSettingsItemBasil() {
    MaterialTheme {
        SettingsScreen(
            selectedColor = ThemeColor.LeafGreen.color,
            pomodoroTime = 0L,
            restTime = 0L,
            autoPlay = true,
            keepScreenOn = true,
            vibrationEnabled = true,
            soundsEnable = true,
            backAction = {},
            onSelectTheme = {},
            setPomodoroTime = {},
            setRestTime = {},
            onAutoPlayChange = {},
            onKeepScreenChange = {},
            onVibrationEnabledChange = {},
            onSoundsEnableChange = {}
        )
    }
}
