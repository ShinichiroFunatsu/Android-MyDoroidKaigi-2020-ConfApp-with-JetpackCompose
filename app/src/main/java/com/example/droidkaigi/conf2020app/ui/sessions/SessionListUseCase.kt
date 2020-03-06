package com.example.droidkaigi.conf2020app.ui.sessions

import com.example.droidkaigi.conf2020app.UseCase
import com.example.droidkaigi.conf2020app.data.DroidKaigiApi
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
    @Suppress("unused")
    suspend fun UseCase.fetchUiSessionListAndGroupedPair(): Pair<List<UiSession>, GroupedUiSessions> {
        return withContext(Dispatchers.IO) {
            val list = droidKaigiApi.fetchTimeTable().toUiSessions()
            list to list.groupBy { it.endsAt.dayOfYear }
                .mapKeys { parDay ->
                    parDay.value[0].startsAt.let {
                        it.toFString(dayOfWeek) to it.toFString(dayFormat)
                    }
                }
                .mapValues { parDay ->
                    parDay.value.groupBy { it.startsAt }
                        .mapKeys { it.key.toFString(hourAndMinutesFormat) }
                }
        }
    }
}

fun TimeTable.toUiSessions(): List<UiSession> = let { timeTable ->
    timeTable.sessions.map { session ->
        val room = timeTable.rooms.first { it.id == session.roomId }
        val category = timeTable.categories.firstOrNull { it.id == session.sessionCategoryItemId }
        val speakers =
            timeTable.speakers.filter { speaker -> session.speakers.any { id -> id == speaker.id } }
        val startsAtDateTime = session.startsAt.toLocaleDateTime()
        val endsAtDateTime = session.endsAt.toLocaleDateTime()
        val dateFromTo = "%s-%s".format(
            startsAtDateTime.toFString(startTimeFormat),
            endsAtDateTime.toFString(endTimeFormat)
        )
        val widthRate: Float = when (session.lengthInMinutes) {
            20 -> 0.65f
            40 -> 0.93f
            60 -> 0.99f
            else -> 0.99f
        } - 0.1f

        UiSession(
            id = SessionId(session.id),
            title = session.title,
            language = session.language,
            description = session.description,
            speakers = speakers,
            message = session.message?.ja,
            targetAudience = session.targetAudience,
            startsAt = startsAtDateTime,
            endsAt = endsAtDateTime,
            dateFromTo = dateFromTo,
            lengthInMinutes = session.lengthInMinutes,

            room = room,
            roomColor = when (room.name.ja) {
                "App bars" -> RoomColors.appBars
                "Backdrop" -> RoomColors.backDrop
                "Cards" -> RoomColors.cards
                "Dialogs" -> RoomColors.dialogs
                "Exhibition" -> RoomColors.exhibition
                "Pickers" -> RoomColors.pickers
                "Sliders" -> RoomColors.slides
                "Tabs" -> RoomColors.tabs
                else -> RoomColors.empty
            },
            sessionCategoryItem = category,
            sessionType = session.sessionType,

            levels = session.levels,
            interpretationTarget = session.interpretationTarget,
            isPlenumSession = session.isPlenumSession,
            isServiceSession = session.isServiceSession,
            asset = session.asset,
            listItemParam = UiSession.ListItemParam(
                widthRate = widthRate
            )
        )
    }
}


private val dateFormat =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.JAPAN)

private val localDateFormat =
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")

private fun String.toDate() = dateFormat.parse(this)
private fun String.toLocaleDate() = LocalDate.parse(this, localDateFormat)
private fun String.toLocaleDateTime() = LocalDateTime.parse(this, localDateFormat)
