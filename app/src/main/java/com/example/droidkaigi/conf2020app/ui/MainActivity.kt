package com.example.droidkaigi.conf2020app.ui

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.animation.Crossfade
import androidx.ui.core.setContent
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import com.example.droidkaigi.conf2020app.AppStatus
import com.example.droidkaigi.conf2020app.Screen
import com.example.droidkaigi.conf2020app.navigateBack
import com.example.droidkaigi.conf2020app.ui.sessions.SessionListScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme() {
                AppContent()
            }
        }
        onBackPressedDispatcher.addCallback {
            navigateBack(onNoPreScreen = {
                this.isEnabled = false; onBackPressed()
            })
        }
    }
}

@Composable
fun AppContent() {
    Crossfade(AppStatus.currentScreen) { screen ->
        Surface(color = (+MaterialTheme.colors()).background) {
            when (screen) {
                is Screen.SessionList -> SessionListScreen()
                is Screen.Detail -> DetailScreen(sessionId = screen.sessionId)
            }
        }
    }
}
