package com.example.droidkaigi.conf2020app.ui.sessions

import androidx.compose.Model
import com.example.droidkaigi.conf2020app.AppStatus
import com.example.droidkaigi.conf2020app.UseCase
import kotlinx.coroutines.*

@Model
class SessionListModel(
    var status: Status
) {

    private var _uiSessions: GroupedUiSessions = mapOf()
    val uiSessions: GroupedUiSessions
        get() = _uiSessions

    sealed class Status {
        object Logo: Status()
        object Loading: Status()
        object Idle: Status()
    }

    fun fetchData() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            loading {
                val (newList, grouped) = UseCase.fetchUiSessionListAndGroupedPair()
                AppStatus.updateSessions(newList)
                _uiSessions = grouped
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