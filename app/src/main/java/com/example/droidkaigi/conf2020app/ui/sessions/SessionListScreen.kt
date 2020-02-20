package com.example.droidkaigi.conf2020app.ui.sessions

import androidx.compose.*
import androidx.ui.core.*
import androidx.ui.foundation.Box
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.layout.LayoutAlign.Bottom
import androidx.ui.material.*
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Card
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.example.droidkaigi.conf2020app.*
import com.example.droidkaigi.conf2020app.ui.UiSession
import com.example.droidkaigi.conf2020app.ui.roomNameText
import com.example.droidkaigi.conf2020app.ui.titleText
import com.example.droidkaigi.conf2020app.ui.sessions.SessionListModel.Status as UiStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

typealias GroupedUiSessions = Map<Pair<String, String>, Map<String, List<UiSession>>>


@Composable
fun SessionListScreen() {
    val model = remember {
        SessionListModel(UiStatus.Logo).apply {
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
            when (model.status) {
                UiStatus.Logo -> LogoScreen()
                UiStatus.Idle -> SimpleSessionList(model.uiSessions)
                UiStatus.Loading -> LoadingScreen()
            }
        }
    }
}

fun LocalDateTime.toFString(formatter: DateTimeFormatter): String = this.format(formatter)
val dayOfWeek: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.US)
val startTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
val endTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
val dayFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.US)
val hourAndMinutesFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

val WeakEmphasis: Emphasis = object : Emphasis {
    override fun emphasize(color: Color) = color.copy(alpha = 0.22f)
}

@Composable
fun SimpleSessionList(groupedUiSessions: GroupedUiSessions) {
    VerticalScroller {
        Column(modifier = LayoutWidth.Fill) {

            // [MM.dd]
            //     [HH:mm]
            //        session A
            //        session B
            // [MM.dd]
            //     [HH:mm]
            //        session C
            //        session D

            val typography = MaterialTheme.typography()
            val emphasisLevels = MaterialTheme.emphasisLevels()

            groupedUiSessions.forEach { (dateStrs: Pair<String, String>, parDay) ->
                Row() {

                    Spacer(modifier = LayoutWidth(8.dp))

                    Column {

                        Spacer(modifier = LayoutHeight(8.dp))

                        Row(modifier = LayoutPadding(16.dp)) {
                            ProvideEmphasis(emphasis = emphasisLevels.medium) {
                                Text(
                                    text = dateStrs.first,
                                    style = typography.h5.copy(
                                        fontWeight = FontWeight.W500
                                    )
                                )
                            }

                            ProvideEmphasis(emphasis = WeakEmphasis) {
                                Text(
                                    text = dateStrs.second,
                                    modifier = Bottom + LayoutPadding(left = 16.dp, bottom = 4.dp),
                                    style = typography.subtitle1.copy(
                                        fontWeight = FontWeight.W500
                                    )
                                )
                            }

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

    ProvideEmphasis(emphasis = WeakEmphasis) {
        Text(
            text = time,
            modifier = LayoutPadding(left = 12.dp),
            style = typography.subtitle2
        )
    }


    Row() {

        Spacer(modifier = LayoutWidth(60.dp))

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
    val typography = MaterialTheme.typography()
    val emphasisLevels = MaterialTheme.emphasisLevels()
    val widthRate: Float = when (session.lengthInMinutes) {
        20 -> 0.65f
        40 -> 0.93f
        60 -> 0.99f
        else -> 0.99f
    } - 0.1f
    Row() {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = LayoutFlexible(widthRate) + LayoutPadding(8.dp),
            color = session.roomColor,
            elevation = 8.dp
        ) {
            Ripple(bounded = true) {
                Clickable(onClick = { navigateTo(Screen.Detail(session.id)) }) {
                    Row {
                        Column(modifier = LayoutPadding(16.dp)) {
                            Text(
                                session.titleText,
                                style = typography.subtitle1
                                    .copy(color = Color.White)
                            )
                            Row(modifier = LayoutPadding(top = 4.dp)) {
                                ProvideEmphasis(emphasis = emphasisLevels.medium) {
                                    Text(
                                        session.roomNameText,
                                        modifier = LayoutPadding(right = 4.dp),
                                        style = typography.caption
                                            .copy(color = Color.White)
                                    )
                                    Text(
                                        dateFromTo,
                                        style = typography.caption
                                            .copy(color = Color.White)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        // TODO change guideline of ConstraintLayout
        Column(modifier = LayoutFlexible(1 - widthRate)) { }
    }
}

@Composable
fun LoadingScreen() {
    Stack(modifier = LayoutSize.Fill) {
        Box(gravity = ContentGravity.Center) {
            Text(text = "Loading")
        }
    }
}

@Composable
fun LogoScreen() {
    Stack(modifier = LayoutSize.Fill) {
        Box(gravity = ContentGravity.Center) {
            Text(text = "Droid Kaigi 2020")
        }
    }
}

@Preview
@Composable
fun preview() {
    LoadingScreen()
}

