package com.example.droidkaigi.conf2020app.data.response

import kotlinx.serialization.Serializable

@Serializable
data class Contributor(
    val icon128Url: String,
    val icon32Url: String,
    val icon64Url: String,
    val iconUrl: String,
    val id: Int,
    val profileUrl: String,
    val sort: Int,
    val username: String
)