package com.example.droidkaigi.conf2020app.ui

import com.example.droidkaigi.conf2020app.data.response.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class UiSession(
    val id: SessionId,
    val title: TitleX,
    val language: String,
    val description: String?,
    val speakers: List<Speaker>,
    val message: String?,
    val targetAudience: String,

    val endsAt: LocalDateTime,
    val startsAt: LocalDateTime,
    val lengthInMinutes: Int,

    val room: Room,
    val sessionCategoryItem: Category?,
    val sessionType: String,

    val levels: List<String>,
    val interpretationTarget: Boolean,
    val isPlenumSession: Boolean,
    val isServiceSession: Boolean,
    val asset: Asset
) {
    fun toPrintString() =
        """
# ${room.name.ja} ($lengthInMinutes min)
            
title: ${title.ja} ($language)
            
description:
 $description

==========================

message: $message
            
        """.trimIndent()
}

data class SessionId(val value: String)


fun TimeTable.toUiSessions(): List<UiSession> = let { timeTable ->
    timeTable.sessions.map { session ->
        val room = timeTable.rooms.first { it.id == session.roomId }
        val category = timeTable.categories.firstOrNull { it.id == session.sessionCategoryItemId }
        val speakers = timeTable.speakers.filter { speaker -> session.speakers.any { id -> id == speaker.id } }

        UiSession(
            id = SessionId(session.id),
            title = session.title,
            language = session.language,
            description = session.description,
            speakers = speakers,
            message = session.message,
            targetAudience = session.targetAudience,

            endsAt = session.endsAt.toLocaleDateTime(),
            startsAt = session.startsAt.toLocaleDateTime(),
            lengthInMinutes = session.lengthInMinutes,

            room = room,
            sessionCategoryItem = category,
            sessionType = session.sessionType,

            levels = session.levels,
            interpretationTarget = session.interpretationTarget,
            isPlenumSession = session.isPlenumSession,
            isServiceSession = session.isServiceSession,
            asset = session.asset
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
