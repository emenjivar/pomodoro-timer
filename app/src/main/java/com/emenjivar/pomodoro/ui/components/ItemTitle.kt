package com.emenjivar.pomodoro.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ItemTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colors.primary,
        style = MaterialTheme.typography.caption,
        modifier = Modifier.padding(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 8.dp
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemTitle() {
    ItemTitle(title = "Time settings")
}
