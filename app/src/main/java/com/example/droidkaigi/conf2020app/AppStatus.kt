package com.example.droidkaigi.conf2020app

import androidx.compose.Model
import androidx.compose.frames.ModelList
import com.example.droidkaigi.conf2020app.data.DroidKaigiApi
import com.example.droidkaigi.conf2020app.ui.SessionId
import com.example.droidkaigi.conf2020app.ui.UiSession
import java.util.*


// Keep Volatility-based Decomposition
@Model
object AppStatus {
    var currentScreen: Screen = Screen.SessionList
    val sessions = ModelList<UiSession>()
    val favorites = ModelList<String>()
    val stack = Stack<Screen>().apply {
        this.push(Screen.SessionList)
    }
}

sealed class Screen {
    object SessionList : Screen()
    data class Detail(val sessionId: SessionId) : Screen()
}

fun navigateTo(destination: Screen, isNeedStack: Boolean = true) {
    if (isNeedStack) AppStatus.stack.push(destination)
    AppStatus.currentScreen = destination
}

fun navigateBack(onNoPreScreen: () -> Unit) {
    with(AppStatus.stack) {
        if (size > 1) {
            pop(); navigateTo(last(), false)
        } else {
            onNoPreScreen()
        }
    }
}

val droidKaigiApi by lazy { DroidKaigiApi }
