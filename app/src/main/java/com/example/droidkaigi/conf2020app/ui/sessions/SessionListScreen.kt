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
import androidx.ui.material.Emphasis
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.TopAppBar
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Card
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.example.droidkaigi.conf2020app.*
import com.example.droidkaigi.conf2020app.ui.*
import com.example.droidkaigi.conf2020app.ui.material.MaterialTheme
import com.example.droidkaigi.conf2020app.ui.material.Typography
import com.example.droidkaigi.conf2020app.ui.sessions.SessionListModel.Status as UiStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

//typealias GroupedUiSessions = Map<Pair<String, String>, Map<String, List<UiSession>>>


@ExperimentalStdlibApi
@Composable
fun SessionListScreen() {
    val model = SessionListModel(UiStatus.Logo).apply {
        fetchData()
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

val dayOfWeek: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.US)
val startTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
val endTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
val dayFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.US)
val hourAndMinutesFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

val WeakEmphasis: Emphasis = object : Emphasis {
    override fun emphasize(color: Color) = color.copy(alpha = 0.22f)
}

@Composable
fun SimpleSessionList(sessionList: SessionList) {
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

            sessionList.dates.forEach { sessionDate ->
                Row() {

                    Spacer(modifier = LayoutWidth(8.dp))

                    Column {

                        Spacer(modifier = LayoutHeight(8.dp))

                        Row(modifier = LayoutPadding(16.dp)) {
                            ProvideEmphasis(emphasis = emphasisLevels.medium) {
                                Text(
                                    text = sessionDate.dateInfo.first,
                                    style = typography.h5.copy(
                                        fontWeight = FontWeight.W500
                                    )
                                )
                            }

                            ProvideEmphasis(emphasis = WeakEmphasis) {
                                Text(
                                    text = sessionDate.dateInfo.second,
                                    modifier = LayoutGravity.Bottom + LayoutPadding(left = 16.dp, bottom = 4.dp),
                                    style = typography.subtitle1.copy(
                                        fontWeight = FontWeight.W500
                                    )
                                )
                            }

                        }

                        sessionDate.sessionTimes.forEach {
                            SessionsSection(typography, it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SessionsSection(typography: Typography, sessions: SessionTime) {

    ProvideEmphasis(emphasis = WeakEmphasis) {
        Text(
            text = sessions.timeInfo,
            modifier = LayoutPadding(left = 12.dp),
            style = typography.subtitle2
        )
    }


    Row() {

        Spacer(modifier = LayoutWidth(60.dp))

        Column() {
            sessions.sessionItems.forEach {
                SessionSimple(session = it)
            }
        }
    }
}

@Composable
fun SessionSimple(session: SessionItem) {

    val typography = MaterialTheme.typography()
    val emphasisLevels = MaterialTheme.emphasisLevels()

    Row() {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = LayoutFlexible(session.widthRate) + LayoutPadding(8.dp),
            color = session.roomColor,
            elevation = 8.dp
        ) {
            Ripple(bounded = true) {
                Clickable(onClick = { navigateTo(Screen.Detail(session.original.id)) }) {
                    Row {
                        Column(modifier = LayoutPadding(16.dp)) {
                            Text(
                                session.title,
                                style = typography.subtitle1
                                    .copy(color = Color.White)
                            )
                            Row(modifier = LayoutPadding(top = 4.dp)) {
                                ProvideEmphasis(emphasis = emphasisLevels.medium) {
                                    Text(
                                        session.room,
                                        modifier = LayoutPadding(right = 4.dp),
                                        style = typography.caption
                                            .copy(color = Color.White)
                                    )
                                    Text(
                                        session.timeInfo,
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
        Column(modifier = LayoutFlexible(1 - session.widthRate)) { }
    }
}

@Composable
fun LoadingScreen() {
    Stack(modifier = LayoutSize.Fill) {
        Box(LayoutGravity.Center) {
            Text(text = "Loading")
        }
    }
}

@Composable
fun LogoScreen() {
    Stack(modifier = LayoutSize.Fill) {
        Box(LayoutGravity.Center) {
            Text(text = "Droid Kaigi 2020")
        }
    }
}

@Preview
@Composable
fun preview() {
    LoadingScreen()
}

