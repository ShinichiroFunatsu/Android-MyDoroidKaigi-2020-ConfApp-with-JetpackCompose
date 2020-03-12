@file:Suppress("FunctionName")

package com.example.droidkaigi.conf2020app.ui.sessions

import com.example.droidkaigi.conf2020app.AppStatus
import com.example.droidkaigi.conf2020app.Screen
import com.example.droidkaigi.conf2020app.UseCase
import com.example.droidkaigi.conf2020app.data.DroidKaigiApi
import com.example.droidkaigi.conf2020app.data.response.Session
import com.example.droidkaigi.conf2020app.data.response.TimeTable
import com.example.droidkaigi.conf2020app.ui.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private val droidKaigiApi by lazy { DroidKaigiApi }

class SessionListUseCaseScope {
    @ExperimentalStdlibApi
    @Suppress("unused")
    suspend fun UseCase.fetchUiSessionListData(): SessionList {
        return withContext(Dispatchers.IO) {
            val timeTable = droidKaigiApi.fetchTimeTable()

            SessionList {
                timeTable.sessions.groupBy { it.endsAt.toLocaleDate().atStartOfDay() }
                    .forEach { (dateTime, parDayList) ->

                        Date(
                            dateInfo = dateTime.format(dayOfWeek) to dateTime.format(dayFormat)
                        ) {
                            AppStatus.updateSessions(
                                buildList<SessionItem> {
                                    parDayList.groupBy { it.startsAt.toLocaleDateTime() }
                                        .forEach { (dateTime, parTimeList) ->

                                            Time(
                                                timeInfo = dateTime.format(hourAndMinutesFormat)
                                            ) {
                                                this@buildList.addAll(

                                                    parTimeList.map { session ->
                                                        SessionItem(
                                                            timeTable,
                                                            session
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                }
                            )
                        }
                    }
            }
        }
    }

    private fun SessionTime.SessionItem(
        timeTable: TimeTable,
        session: Session
    ): SessionItem {
        val roomName = timeTable.rooms
            .first { it.id == session.roomId }
            .name.ja

        return SessionItem {
            title = session.title.ja
            room = roomName
            roomColor = when (roomName) {
                "App bars" -> RoomColors.appBars
                "Backdrop" -> RoomColors.backDrop
                "Cards" -> RoomColors.cards
                "Dialogs" -> RoomColors.dialogs
                "Exhibition" -> RoomColors.exhibition
                "Pickers" -> RoomColors.pickers
                "Sliders" -> RoomColors.slides
                "Tabs" -> RoomColors.tabs
                else -> RoomColors.empty
            }
            timeInfo = "%s-%s".format(
                session.startsAt.format(startTimeFormat),
                session.endsAt.format(endTimeFormat)
            )
            widthRate =
                when (session.lengthInMinutes) {
                    20 -> 0.65f
                    40 -> 0.93f
                    60 -> 0.99f
                    else -> 0.99f
                } - 0.1f
            original = session

        }
    }
}

private val dateFormat =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.JAPAN)

private val localDateFormat =
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")

private fun String.toDate() = dateFormat.parse(this)
private fun String.toLocaleDate() = LocalDate.parse(this, localDateFormat)
private fun String.toLocaleDateTime() = LocalDateTime.parse(this, localDateFormat)
