package com.example.droidkaigi.conf2020app.data.response

import com.example.droidkaigi.conf2020app.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration


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

@KtorExperimentalAPI
object DroidKaigiApi {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json(
                    JsonConfiguration.Stable.copy(strictMode = false)
                )
            )
        }
    }
    private val json = Json(JsonConfiguration.Stable.copy(strictMode = false))
    suspend fun fetchTimeTable(): TimeTable {
        return client.get<String> {
            url("$apiEndpoint/timetable")
            accept(ContentType.Application.Json)
        }.let { json.parse(TimeTable.serializer(), it) }
    }
}