package com.emenjivar.pomodoro.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource

@Composable
fun Int.toColor() = colorResource(id = this)

@Composable
fun Int.toIcon() = painterResource(id = this)
