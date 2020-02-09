package com.example.droidkaigi.conf2020app

import androidx.compose.Model
import androidx.compose.frames.ModelList
import com.example.droidkaigi.conf2020app.data.DroidKaigiApi
import com.example.droidkaigi.conf2020app.ui.SessionId
import com.example.droidkaigi.conf2020app.ui.SessionListModel
import com.example.droidkaigi.conf2020app.ui.UiSession


// Keep Volatility-based Decomposition
@Model
object AppStatus {
    var currentScreen: Screen = Screen.SessionList
    val sessions = ModelList<UiSession>()
    val favorites = ModelList<String>()
}

sealed class Screen {
    object SessionList : Screen()
    data class Detail(val sessionId: SessionId) : Screen()
}

fun SessionListModel.navigateTo(destination: Screen) {
    AppStatus.currentScreen = destination
}

val droidKaigiApi by lazy { DroidKaigiApi }
