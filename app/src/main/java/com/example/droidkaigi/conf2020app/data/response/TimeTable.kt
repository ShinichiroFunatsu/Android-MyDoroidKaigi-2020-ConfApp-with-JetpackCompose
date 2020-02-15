package com.example.droidkaigi.conf2020app.data.response


data class TimeTable(
    val categories: List<Category>,
    val questions: List<Question>,
    val rooms: List<Room>,
    val sessions: List<Session>,
    val speakers: List<Speaker>
)

data class Category(
    val id: Int,
    val items: List<Item>,
    val sort: Int,
    val title: Title
)

data class Item(
    val id: Int,
    val name: Name,
    val sort: Int
)

data class Name(
    val en: String,
    val ja: String
)

data class Title(
    val en: String,
    val ja: String
)

data class Question(
    val id: Int,
    val question: QuestionX,
    val questionType: String,
    val sort: Int
)

data class QuestionX(
    val en: String,
    val ja: String
)

data class Room(
    val id: Int,
    val name: NameX,
    val sort: Int
)

data class NameX(
    val en: String,
    val ja: String
)

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
    val message: Message?,
    val roomId: Int,
    val sessionCategoryItemId: Int,
    val sessionType: String,
    val speakers: List<String>,
    val startsAt: String,
    val targetAudience: String,
    val title: TitleX
)

data class Message(
    val en: String,
    val ja: String
)

data class Asset(
    val slideUrl: String?,
    val videoUrl: String?
)

data class TitleX(
    val en: String,
    val ja: String
)

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