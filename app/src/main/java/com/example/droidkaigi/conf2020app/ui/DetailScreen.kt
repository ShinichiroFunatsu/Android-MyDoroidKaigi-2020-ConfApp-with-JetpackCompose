package com.example.droidkaigi.conf2020app.ui

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.LayoutHeight
import androidx.ui.layout.LayoutPadding
import androidx.ui.material.TopAppBar
import androidx.ui.unit.dp
import com.example.droidkaigi.conf2020app.AppStatus
import com.example.droidkaigi.conf2020app.ui.material.MaterialTheme


@Composable
fun DetailScreen(
    sessionId: SessionId
) {

    val uiSession: UiSession = AppStatus.sessions.firstOrNull { uiSession -> uiSession.id == sessionId }
        ?: throw IllegalArgumentException("not found session: $sessionId")

    Column {
        TopAppBar(
            title = {
                Text(text = "Detail")
            }
        )
        VerticalScroller(modifier = LayoutHeight.Fill + LayoutPadding(8.dp)) {
            Column {
                Text(text = uiSession.toPrintString(), style = MaterialTheme.typography().body2)
            }
        }
    }
}
