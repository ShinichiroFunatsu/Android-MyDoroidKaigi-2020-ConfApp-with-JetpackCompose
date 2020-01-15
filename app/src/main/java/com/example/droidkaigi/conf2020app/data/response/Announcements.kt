package com.example.droidkaigi.conf2020app.data.response

import kotlinx.serialization.Serializable

@Serializable
data class Announcement(
    val content: String,
    val id: Int,
    val isWithdrawn: Boolean,
    val language: String,
    val publishedAt: String,
    val title: String,
    val type: String
)