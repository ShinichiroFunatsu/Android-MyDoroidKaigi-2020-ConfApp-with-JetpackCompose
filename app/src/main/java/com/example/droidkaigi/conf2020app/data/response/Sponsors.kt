package com.example.droidkaigi.conf2020app.data.response

import kotlinx.serialization.Serializable

@Serializable
data class Sponsor(
    val companyLogoUrl: String,
    val companyName: CompanyName,
    val companyUrl: String,
    val hasBooth: Boolean,
    val id: String,
    val plan: String,
    val planDetail: String,
    val sort: Int
)

@Serializable
data class CompanyName(
    val en: String,
    val ja: String
)