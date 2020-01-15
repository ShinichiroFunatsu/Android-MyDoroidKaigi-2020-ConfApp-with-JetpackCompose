package com.example.droidkaigi.conf2020app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.example.droidkaigi.conf2020app.data.response.RemoteDataSource
import com.example.droidkaigi.conf2020app.data.response.TimeTable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val remoteDataSource by lazy {  RemoteDataSource() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var timeTable: TimeTable? = null
        GlobalScope.launch {
            timeTable = remoteDataSource.fetchTimeTable()
            Log.d("ababab", timeTable.toString())
        }
        setContent {
            MaterialTheme {
                Greeting("Android")
            }
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
