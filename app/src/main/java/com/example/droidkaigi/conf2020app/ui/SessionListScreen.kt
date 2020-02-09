package com.example.droidkaigi.conf2020app.ui

import androidx.compose.*
import androidx.compose.frames.ModelList
import androidx.ui.core.Alignment
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.Spacing
import androidx.ui.layout.Stack
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.withOpacity
import androidx.ui.tooling.preview.Preview
import com.example.droidkaigi.conf2020app.AppStatus
import com.example.droidkaigi.conf2020app.droidKaigiApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Model
class SessionListModel {
    var status: Status = Status.Logo
    val sessions: ModelList<UiSession>
        get() = AppStatus.sessions

    sealed class Status {
        object Logo: Status()
        object Loading: Status()
        object Idle: Status()
    }

    fun fetchData() {
        status = Status.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val new = withContext(Dispatchers.IO) {
                droidKaigiApi.fetchTimeTable().toUiSessions()
            }
            withContext(Dispatchers.Main) {
                sessions.addAll(new)
                status = Status.Idle
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
fun LogoScreen() {
    Stack {
        aligned(alignment = Alignment.Center) {
            Text(text = "Droid Kaigi 2020")
        }
    }
}

@Composable
fun SessionListScreen() {
    val model = +memo { SessionListModel() }

    +onActive {
        model.fetchData()
    }

    Column {
        TopAppBar(
            title = { Text(text = "DroidKaigi 2020") }
        )
        VerticalScroller(modifier = Flexible(1f)) {
            Column {
                when(model.status) {
                    SessionListModel.Status.Logo ->
                        LogoScreen()
                    SessionListModel.Status.Idle ->
                        SimpleSessionList(sessions = model.sessions)
                    SessionListModel.Status.Loading ->
                        LoadingScreen()
                }
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
fun SessionSimple(session: UiSession, onClick: (() -> Unit)? = null) {
    Ripple(bounded = true) {
        Clickable(onClick) {
            Row(modifier = Spacing(16.dp)) {
                Column(modifier = Flexible(1f)) {
                    Text(
                        session.title.ja,
                        style = ((+MaterialTheme.typography()).subtitle1).withOpacity(0.87f)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun preview() {
    LoadingScreen()
}
