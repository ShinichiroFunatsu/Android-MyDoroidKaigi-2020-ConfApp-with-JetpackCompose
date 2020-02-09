package com.example.droidkaigi.conf2020app.ui

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.material.TopAppBar
import com.example.droidkaigi.conf2020app.AppStatus


@Composable
fun DetailScreen(
    sessionId: SessionId
) {

    val uiSession: UiSession = AppStatus.sessions.firstOrNull { uiSession -> uiSession.id == sessionId }
        ?: throw IllegalArgumentException("not found session: $sessionId")

    Column {
        TopAppBar(
            title = { Text(text = "Detail") }
        )
        VerticalScroller(modifier = Flexible(1f)) {
            Column {
                Text(text = uiSession.toString())
            }
        }
    }
}
