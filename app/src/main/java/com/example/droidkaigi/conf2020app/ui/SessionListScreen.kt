package com.example.droidkaigi.conf2020app.ui

import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.frames.ModelList
import androidx.compose.memo
import androidx.compose.onActive
import androidx.compose.unaryPlus
import androidx.ui.core.Alignment
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.withOpacity
import androidx.ui.tooling.preview.Preview
import com.example.droidkaigi.conf2020app.AppStatus
import com.example.droidkaigi.conf2020app.Screen
import com.example.droidkaigi.conf2020app.droidKaigiApi
import com.example.droidkaigi.conf2020app.navigateTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Model
class SessionListModel(
        var status: Status
) {
    val sessions: ModelList<UiSession>
        get() = AppStatus.sessions

    sealed class Status {
        object Logo: Status()
        object Loading: Status()
        object Idle: Status()
    }

    fun fetchData() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            status = Status.Loading
            val new = withContext(Dispatchers.IO) {
                droidKaigiApi.fetchTimeTable().toUiSessions()
            }
            sessions.clear()
            sessions.addAll(new)
            status = Status.Idle
        }
    }
}
@Composable
fun SessionListScreen() {
    val model = +memo {
        SessionListModel(SessionListModel.Status.Logo).apply {
            fetchData()
        }
    }

// no need fetch on each active
//    +onActive {
//        model.fetchData()
//    }

    Column {
        TopAppBar(
            title = { Text(text = "DroidKaigi 2020") }
        )
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

fun LocalDateTime.toFormatStr(formatter: DateTimeFormatter): String = this.format(formatter)
val startTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
val endTimeFormat: DateTimeFormatter =  DateTimeFormatter.ofPattern("HH:mm")
val dayFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("M.dd")
val hourAndMinutesFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun SimpleSessionList(sessions: List<UiSession>) {
    VerticalScroller {
        Column(modifier = Expanded) {

            // [MM.dd]
            //     [HH:mm]
            //        session A
            //        session B
            // [MM.dd]
            //     [HH:mm]
            //        session C
            //        session D

            val dayParStartTimePerSessions: Map<String, Map<String, List<UiSession>>> =
                sessions.groupBy { it.endsAt.dayOfYear }
                    .mapKeys { parDay -> parDay.value[0].startsAt.toFormatStr(dayFormat) }
                    .mapValues { parDay ->
                        parDay.value.groupBy { it.startsAt }
                            .mapKeys { it.key.toFormatStr(hourAndMinutesFormat) }
                    }

            dayParStartTimePerSessions.forEach { (date: String, parDay) ->
                Column() {
                    Text(date) 
                    parDay.forEach { (time, sessions) ->
                        Text(time)
                        sessions.forEach {
                            SessionSimple(session = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SessionSimple(session: UiSession) {
    val dateFromTo = "${session.startsAt.toFormatStr(startTimeFormat)}-${session.endsAt.toFormatStr(
        endTimeFormat)}"
    Ripple(bounded = true) {
        Clickable(onClick = { navigateTo(Screen.Detail(session.id))}) {
            Row {
                Column(modifier = Flexible(1f) wraps Spacing(12.dp)) {
                    Text(
                        session.title.ja,
                        style = ((+MaterialTheme.typography()).h6).withOpacity(0.87f)
                    )
                    Text(
                        dateFromTo,
                        modifier = Spacing(4.dp),
                        style = ((+MaterialTheme.typography()).caption).withOpacity(0.33f)
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Stack(modifier = Expanded) {
        aligned(alignment = Alignment.Center) {
            Text(text = "Loading")
        }
    }
}

@Composable
fun LogoScreen() {
    Stack(modifier = Expanded) {
        aligned(alignment = Alignment.Center) {
            Text(text = "Droid Kaigi 2020")
        }
    }
}


@Preview
@Composable
fun preview() {
    LoadingScreen()
}

