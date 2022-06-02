package com.emenjivar.pomodoro.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.emenjivar.pomodoro.R

@Composable
fun Int.toColor() = colorResource(id = if(this != 0) this else R.color.primary)

@Composable
fun Int.toIcon() = painterResource(id = this)
