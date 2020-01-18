package com.example.droidkaigi.conf2020app.data.response

import kotlinx.serialization.Serializable

@Serializable
data class TimeTable(
    val categories: List<Category>,
    val questions: List<Question>,
    val rooms: List<Room>,
    val sessions: List<Session>,
    val speakers: List<Speaker>
)

@Serializable
data class Category(
    val id: Int,
    val items: List<Item>,
    val sort: Int,
    val title: Title
)

@Serializable
data class Item(
    val id: Int,
    val name: Name,
    val sort: Int
)

@Serializable
data class Name(
    val en: String,
    val ja: String
)

@Serializable
data class Title(
    val en: String,
    val ja: String
)

@Serializable
data class Question(
    val id: Int,
    val question: QuestionX,
    val questionType: String,
    val sort: Int
)

@Serializable
data class QuestionX(
    val en: String,
    val ja: String
)

@Serializable
data class Room(
    val id: Int,
    val name: NameX,
    val sort: Int
)

@Serializable
data class NameX(
    val en: String,
    val ja: String
)

@Serializable
data class Session(
    val asset: Asset,
    val description: String?,
    val endsAt: String,
    val id: String,
    val interpretationTarget: Boolean,
    val isPlenumSession: Boolean,
    val isServiceSession: Boolean,
    val language: String,
    val lengthInMinutes: Int,
    val levels: List<String>,
    val message: String?,
    val roomId: Int,
    val sessionCategoryItemId: Int,
    val sessionType: String,
    val speakers: List<String>,
    val startsAt: String,
    val targetAudience: String,
    val title: TitleX
)

@Serializable
data class Asset(
    val slideUrl: String?,
    val videoUrl: String?
)

@Serializable
data class TitleX(
    val en: String,
    val ja: String
)

@Serializable
data class Speaker(
    val bio: String,
    val firstName: String?,
    val fullName: String?,
    val id: String,
    val isTopSpeaker: Boolean,
    val lastName: String,
    val profilePicture: String?,
    val sessions: List<String>,
    val tagLine: String
)