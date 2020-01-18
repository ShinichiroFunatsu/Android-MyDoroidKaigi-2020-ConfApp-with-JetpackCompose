package com.example.droidkaigi.conf2020app.data

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class Client(val endPoint: String) {
    private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    private var client = OkHttpClient()

    fun post(path: String, json: String) =
        client.call {
            request {
                url("$endPoint/$path")
                post(json.toRequestBody(JSON))
            }
        }

    fun run(path: String) =
        client.call {
            request { url("$endPoint/$path") }
        }

    private fun request(f: Request.Builder.() -> Unit): Request {
        return Request.Builder().apply { f() }.build()
    }

    private fun OkHttpClient.call(request: () -> Request): String {
        newCall(request()).execute().use { response -> return response.body!!.string() }
    }

}