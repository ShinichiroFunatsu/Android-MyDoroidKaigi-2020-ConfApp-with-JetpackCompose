package com.example.droidkaigi.conf2020app.data.response

import com.example.droidkaigi.conf2020app.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path


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
const val url = BuildConfig.API_ENDPOINT

interface DroidKaigiApi {

    @GET("timetable")
    suspend fun fetchTimeTable(): TimeTable

    @GET("announcements")
    suspend fun fetchAnnouncements(): List<Announcement>

    @GET("contributors")
    suspend fun fetchContributors(): List<Contributor>
}

class RemoteDataSource {
    val contentType = MediaType.get("application/json")
    val retrofit =  Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory(contentType))
        .baseUrl(url)
        .build()
    val service = retrofit.create(DroidKaigiApi::class.java)

    suspend fun fetchTimeTable(): TimeTable {
        return service.fetchTimeTable()
    }


}
