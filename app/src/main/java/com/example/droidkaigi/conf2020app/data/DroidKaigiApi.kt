package com.example.droidkaigi.conf2020app.data

import android.util.Log
import com.example.droidkaigi.conf2020app.BuildConfig
import com.example.droidkaigi.conf2020app.data.response.TimeTable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


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
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

   private  var timeTableAdapter =
        moshi.adapter<TimeTable>(TimeTable::class.java)

    private val client =
        Client(apiEndpoint)

    fun fetchTimeTable(): TimeTable {
        val payload: String = client.run("timetable")
        return timeTableAdapter.fromJson(payload)!!
    }
}
