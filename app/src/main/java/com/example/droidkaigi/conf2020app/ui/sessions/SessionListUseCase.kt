package com.example.droidkaigi.conf2020app.ui.sessions

import com.example.droidkaigi.conf2020app.UseCase
import com.example.droidkaigi.conf2020app.data.DroidKaigiApi
import com.example.droidkaigi.conf2020app.ui.UiSession
import com.example.droidkaigi.conf2020app.ui.toUiSessions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val droidKaigiApi by lazy { DroidKaigiApi }

class SessionListUseCaseScope {
    suspend fun UseCase.fetchUiSessionListAndGroupedPair(): Pair<List<UiSession>, GroupedUiSessions> {
        return withContext(Dispatchers.IO) {
            val list = droidKaigiApi.fetchTimeTable().toUiSessions()
            list to list.groupBy { it.endsAt.dayOfYear }
                .mapKeys { parDay ->
                    parDay.value[0].startsAt.let {
                        it.toFString(dayOfWeek) to it.toFString(dayFormat)
                    }
                }
                .mapValues { parDay ->
                    parDay.value.groupBy { it.startsAt }
                        .mapKeys { it.key.toFString(hourAndMinutesFormat) }
                }
        }
    }
}