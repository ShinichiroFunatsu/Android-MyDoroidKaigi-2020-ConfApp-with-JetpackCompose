package com.example.droidkaigi.conf2020app.ui.sessions

import androidx.compose.Model
import com.example.droidkaigi.conf2020app.AppStatus
import com.example.droidkaigi.conf2020app.UseCase
import com.example.droidkaigi.conf2020app.ui.SessionList
import kotlinx.coroutines.*

@Model
class SessionListModel(
    var status: Status
) {

    private lateinit var _uiSessions: SessionList
    val uiSessions: SessionList
        get() = _uiSessions

    sealed class Status {
        object Logo: Status()
        object Loading: Status()
        object Idle: Status()
    }

    @ExperimentalStdlibApi
    fun fetchData() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            loading {
                val sessionList = UseCase.fetchUiSessionListData()
                _uiSessions = sessionList
            }
        }
    }
}

private suspend fun SessionListModel.loading(block: suspend SessionListUseCaseScope.()-> Unit) {
    withContext(Dispatchers.Main)  {
        status = SessionListModel.Status.Loading
        useCaseScope {
            block()
        }
        status = SessionListModel.Status.Idle
    }
}

private suspend fun SessionListModel.useCaseScope(block: suspend SessionListUseCaseScope.()-> Unit) =
    SessionListUseCaseScope().block()