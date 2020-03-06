package com.example.droidkaigi.conf2020app.ui

import androidx.ui.graphics.Color
import com.example.droidkaigi.conf2020app.data.response.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    val dateFromTo: String,
    val lengthInMinutes: Int,

    val room: Room,
    val roomColor: Color,
    val sessionCategoryItem: Category?,
    val sessionType: String,

    val levels: List<String>,
    val interpretationTarget: Boolean,
    val isPlenumSession: Boolean,
    val isServiceSession: Boolean,
    val asset: Asset,

    val listItemParam: ListItemParam
) {
    data class ListItemParam(
        val widthRate: Float
    )
}

data class SessionId(val value: String)

val UiSession.titleText: String
    get() = title.ja
val UiSession.roomNameText: String
    get() = room.name.ja

fun LocalDateTime.toFString(formatter: DateTimeFormatter): String = this.format(formatter)

fun UiSession.toPrintString() = """
# ${room.name.ja} ($lengthInMinutes min)
            
title: ${title.ja} ($language)
            
description:
 $description

==========================

message: $message
            
        """.trimIndent()

