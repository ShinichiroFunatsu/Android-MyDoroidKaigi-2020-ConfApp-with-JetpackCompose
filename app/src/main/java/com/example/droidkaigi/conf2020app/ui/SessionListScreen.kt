package com.example.droidkaigi.conf2020app.ui

import android.util.Log
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.frames.ModelList
import androidx.compose.memo
import androidx.compose.onActive
import androidx.compose.unaryPlus
import androidx.ui.core.*
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.Typography
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Card
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
import java.util.*

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
//    val typography = +MaterialTheme.typography()
    Column {
        TopAppBar(
            title = {
                Text(text = "DroidKaigi 2020")
            }
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

fun LocalDateTime.toFString(formatter: DateTimeFormatter): String = this.format(formatter)
val dayOfWeek: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.US)
val startTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
val endTimeFormat: DateTimeFormatter =  DateTimeFormatter.ofPattern("HH:mm")
val dayFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.US)
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

            val dayParStartTimePerSessions: Map<Pair<String, String>, Map<String, List<UiSession>>> =
                sessions.groupBy { it.endsAt.dayOfYear }
                    .mapKeys { parDay ->
                        parDay.value[0].startsAt.let {
                            it.toFString(dayOfWeek) to it.toFString(dayFormat)
                        }
                    }
                    .mapValues { parDay ->
                        parDay.value.groupBy { it.startsAt }
                            .mapKeys { it.key.toFString(hourAndMinutesFormat) }
                    }
            val typography = +MaterialTheme.typography()

            dayParStartTimePerSessions.forEach { (dateStrs: Pair<String, String>, parDay) ->
                Row() {

                    WidthSpacer(width = 8.dp)

                    Column() {
                        Row() {
                            Text(
                                text = dateStrs.first,
                                modifier = Spacing(8.dp),
                                style = typography.h5
                                    .withOpacity(0.87f)
                            )
                            Text(
                                text = dateStrs.second,
                                modifier = Spacing(8.dp) wraps Gravity.Bottom,
                                style = typography.subtitle1
                                    .withOpacity(0.33f)
                            )
                        }
                        parDay.forEach { (time, sessions) ->

                            SessionsSection(time, typography, sessions)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SessionsSection(time: String, typography: Typography, sessions: List<UiSession>) {
    Text(
        text = time,
        modifier = Spacing(left = 12.dp),
        style = typography.subtitle2
            .withOpacity(0.33f)
    )

    Row() {

        WidthSpacer(width = 60.dp)

        Column() {
            sessions.forEach {
                SessionSimple(session = it)
            }
        }
    }
}

@Composable
fun SessionSimple(session: UiSession) {
    val dateFromTo = "%s-%s".format(
        session.startsAt.toFString(startTimeFormat),
        session.endsAt.toFString(endTimeFormat)
    )
    val typography = +MaterialTheme.typography()
    val widthRate: Float = when (session.lengthInMinutes) {
        20 -> 0.6f
        40 -> 0.93f
        60 -> 0.99f
        else -> 0.99f
    } - 0.1f
    Row() {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Flexible(widthRate) wraps Spacing(8.dp),
            color = session.roomColor,
            elevation = 8.dp
        ) {
            Ripple(bounded = true) {
                Clickable(onClick = { navigateTo(Screen.Detail(session.id)) }) {
                    Row {
                        Column(modifier = Spacing(16.dp)) {
                            Text(
                                session.titleText,
                                style = typography.subtitle1
                                    .copy(color = Color.White)
                                    .withOpacity(0.87f)
                            )
                            Row(modifier = Spacing(top = 4.dp)) {
                                Text(
                                    session.roomNameText,
                                    modifier = Spacing(right = 4.dp),
                                    style = typography.caption
                                        .copy(color = Color.White)
                                        .withOpacity(0.6f)
                                )
                                Text(
                                    dateFromTo,
                                    style = typography.caption
                                        .copy(color = Color.White)
                                        .withOpacity(0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
        Column(modifier = Flexible(1-widthRate)) {

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

