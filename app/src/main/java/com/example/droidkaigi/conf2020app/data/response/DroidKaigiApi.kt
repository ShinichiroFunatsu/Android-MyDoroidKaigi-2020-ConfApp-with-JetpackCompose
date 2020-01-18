package com.example.droidkaigi.conf2020app.data.response

import com.example.droidkaigi.conf2020app.BuildConfig
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


/*

debug https://deploy-preview-49--droidkaigi-api-dev.netlify.com/2020
release https://api.droidkaigi.jp/2020

/timetable
/announcements/jp
/announcements/en
/sponsors
/staffs
/contributors


*/
const val apiEndpoint = BuildConfig.API_ENDPOINT

object DroidKaigiApi {
    private val client = Client(apiEndpoint)
    fun fetchTimeTable(): TimeTable {
        val payload = client.run("timetable")
        val json = Json(JsonConfiguration.Stable)
        return json.parse(TimeTable.serializer(), payload)
    }
}
