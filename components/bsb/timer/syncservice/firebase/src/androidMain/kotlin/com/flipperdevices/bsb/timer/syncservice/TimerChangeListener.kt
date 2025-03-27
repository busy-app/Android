package com.flipperdevices.bsb.timer.syncservice

import com.flipperdevices.bsb.cloud.mock.api.BSBMockApi
import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
class TimerChangeListener(
    private val scope: CoroutineScope,
    private val timerApi: TimerApi,
    private val bsbMockApi: BSBMockApi,
    private val krateApi: KrateApi
) : LogTagProvider {
    override val TAG: String = "MyFirebaseMessagingService"

    private fun listenForTimerChange() {
        timerApi.getTimestampState()
            .distinctUntilChanged { old, new -> old == new }
            .filter { bsbMockApi.getTimer().getOrNull() != it }
            .onEach { bsbMockApi.saveTimer(it) }
            .launchIn(scope)
    }

    private fun listenForTokenChange() {
        info { "#onCreate" }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    error(task.exception) { "#addOnCompleteListener" }
                    return@OnCompleteListener
                }
                val token = task.result
                info { "#addOnCompleteListener: $token" }
                scope.launch {
                    krateApi.firebaseTokenKrate.save(token)
                    bsbMockApi.auth()
                }
            }
        )
    }

    fun onCreate() {
        listenForTimerChange()
        listenForTokenChange()
    }
}
