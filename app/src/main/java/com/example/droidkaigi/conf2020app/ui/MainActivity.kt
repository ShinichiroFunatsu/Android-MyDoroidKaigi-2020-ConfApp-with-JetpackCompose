package com.example.droidkaigi.conf2020app.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.example.droidkaigi.conf2020app.data.DroidKaigiApi
import com.example.droidkaigi.conf2020app.data.response.TimeTable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val state = State()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state.getTimeTable()
        setContent {
            MaterialTheme {
                Greeting("Android")
            }
        }
    }
}

@Model
class State {
    val droidKaigiApi by lazy { DroidKaigiApi }
    fun getTimeTable() {
        var timeTable: TimeTable? = null
        GlobalScope.launch {
            timeTable = droidKaigiApi.fetchTimeTable()
            Log.d("ababab", timeTable.toString())
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Greeting("Android")
    }
}
