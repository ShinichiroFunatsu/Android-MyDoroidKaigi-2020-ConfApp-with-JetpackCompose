package com.example.droidkaigi.conf2020app

import androidx.compose.Model
import androidx.compose.frames.ModelList
import com.example.droidkaigi.conf2020app.ui.SessionItem
import com.example.droidkaigi.conf2020app.ui.SessionList
import java.util.*


// Keep Volatility-based Decomposition
@Model
object AppStatus {
    var currentScreen: Screen = Screen.SessionList
    lateinit var sessions: List<SessionItem>
    val favorites = ModelList<String>()
    val stack = Stack<Screen>().apply {
        this.push(Screen.SessionList)
    }

    fun updateSessions(new: List<SessionItem> ) {
        sessions = new
    }
}

sealed class Screen {
    object SessionList : Screen()
    data class Detail(val sessionId: String) : Screen()
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
