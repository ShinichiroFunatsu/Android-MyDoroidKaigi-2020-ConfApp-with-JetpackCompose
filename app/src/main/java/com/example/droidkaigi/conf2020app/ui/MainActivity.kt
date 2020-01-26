package com.example.droidkaigi.conf2020app.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.frames.ModelList
import androidx.compose.unaryPlus
import androidx.ui.animation.Crossfade
import androidx.ui.core.*
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.Spacing
import androidx.ui.layout.Stack
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Surface
import androidx.ui.material.withOpacity
import androidx.ui.tooling.preview.Preview
import com.example.droidkaigi.conf2020app.VectorImageButton
import com.example.droidkaigi.conf2020app.data.DroidKaigiApi
import com.example.droidkaigi.conf2020app.data.response.TimeTable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DroidKaigiAStatus.updateSessions()
        setContent {
            MaterialTheme {
                AppContent()
            }
        }
    }
}

val droidKaigiApi by lazy { DroidKaigiApi }

sealed class Screen {
    sealed class SessionList : Screen() {
        object Loading : SessionList()
        object Idle : SessionList()
    }

    data class Detail(val session: UiSession) : Screen()
}

@Model
object DroidKaigiAStatus {
    val sessions = ModelList<UiSession>()
    val favorites = ModelList<String>()
    var currentScreen: Screen = Screen.SessionList.Loading

    fun updateSessions() {
        GlobalScope.launch {
            Log.d("ababab", "Loading")
            val new = droidKaigiApi.fetchTimeTable().toUiSessions()
//            navigateTo(Screen.SessionList.Loading)
            sessions.addAll(new)
            Log.d("ababab", "Loaded")
            navigateTo(Screen.SessionList.Idle)
        }
    }
}

fun navigateTo(destination: Screen) {
    DroidKaigiAStatus.currentScreen = destination
}


@Composable
fun AppContent() {
    Crossfade(DroidKaigiAStatus.currentScreen) { screen ->
        Surface(color = (+MaterialTheme.colors()).background) {
            when (screen) {
                is Screen.SessionList -> {
                    when (screen) {
                        is Screen.SessionList.Loading -> LoadingScreen()
                        is Screen.SessionList.Idle -> SessionListScreen()
                    }
                }
                is Screen.Detail -> DetailScreen(session = screen.session)
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Stack {
        aligned(alignment = Alignment.Center) {
            Text(text = "Loading")
        }
    }
}

@Composable
fun SessionListScreen() {
    val sessions = DroidKaigiAStatus.sessions

    Column {
        TopAppBar(
            title = { Text(text = "DroidKaigi 2020") }
        )
        VerticalScroller(modifier = Flexible(1f)) {
            Column {
                SimpleSessionList(sessions = sessions)
            }
        }
    }
}

@Composable
fun SimpleSessionList(sessions: List<UiSession>) {
    sessions.forEach {
        SessionSimple(it)
    }
}

@Composable
fun SessionSimple(session: UiSession) {
    Ripple(bounded = true) {
        Clickable(onClick = {
            navigateTo(Screen.Detail(session))
        }) {
            Row(modifier = Spacing(16.dp)) {
                Column(modifier = Flexible(1f)) {
                    Text(session.title.ja, style = ((+MaterialTheme.typography()).subtitle1).withOpacity(0.87f))
                }
            }
        }
    }
}


fun DetailScreen(
    session: UiSession
) {
    Column {
        TopAppBar(
            title = { Text(text = "Detail") }
        )
        VerticalScroller(modifier = Flexible(1f)) {
            Column {
                Text(text = session.toString())
            }
        }
    }
}


@Preview
@Composable
fun preview() {
    LoadingScreen()
}
