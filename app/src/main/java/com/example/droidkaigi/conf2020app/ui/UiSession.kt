@file:Suppress("FunctionName")

package com.example.droidkaigi.conf2020app.ui

import androidx.ui.graphics.Color
import com.example.droidkaigi.conf2020app.data.response.*
import kotlin.properties.Delegates


fun SessionList(block: SessionList.() -> Unit): SessionList {
    return SessionList().apply {
        block()
    }
}

class SessionList {
    val dates = mutableListOf<SessionDate>()
    fun Date(dateInfo: Pair<String, String>, block: SessionDate.() -> Unit) {
        dates.add(SessionDate(dateInfo).apply { block() })
    }
}

class SessionDate(
    val dateInfo: Pair<String, String>
) {
    val sessionTimes = mutableListOf<SessionTime>()
    fun Time(timeInfo: String, block: SessionTime.() -> Unit) {
        sessionTimes.add(SessionTime(timeInfo).apply { block() })
    }
}

class SessionTime(
    val timeInfo: String
) {
    val sessionItems = mutableListOf<SessionItem>()
    fun SessionItem(block: SessionItem.() -> Unit): SessionItem {
        return SessionItem().apply { block() }.also {
            sessionItems.add(it)
        }
    }
}

class SessionItem {
    lateinit var title: String
    lateinit var room: String
    lateinit var roomColor: Color
    lateinit var timeInfo: String
    lateinit var original: Session
    var widthRate by Delegates.notNull<Float>()
}

fun SessionItem.toPrintString() = """
# $room (${original.lengthInMinutes} min)
            
title: ${title} (${original.language})
            
description:
 ${original.description}

==========================

        """.trimIndent()