package com.flipperdevices.core.trustedclock

import android.content.Context
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.flipperdevices.core.log.warn
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.time.TrustedTime
import com.google.android.gms.time.TrustedTimeClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TrustedClock::class)
class GoogleTrustedTimeProvider(
    private val context: Context
) : TrustedClock, LogTagProvider, OnCompleteListener<TrustedTimeClient> {
    override val TAG: String = "GoogleTrustedTimeProvider"

    private var trustedClient: TrustedTimeClient? = null

    override fun initialize() {
        info { "Create task on trusted time api initialize" }
        val clientTask = TrustedTime.createClient(context)
        clientTask.addOnCompleteListener(this)
    }

    override fun onComplete(taskResult: Task<TrustedTimeClient>) {
        if (taskResult.isSuccessful) {
            info { "Trusted time client initialize successfully" }
            trustedClient = taskResult.result
            return
        }
        error(taskResult.exception) { "Fail create task" }
    }

    override fun now(): Instant {
        val localTrustedClient = trustedClient
        if (localTrustedClient == null) {
            warn { "Requested the exact time, but the timer api is not yet initialised" }
            return Clock.System.now()
        }

        val instant = localTrustedClient.computeCurrentInstant()
            ?.toKotlinInstant()
        if (instant == null) {
            warn { "TrustedTimeClient is initialized, but current instant is incorrect" }
            return Clock.System.now()
        }

        return instant
    }
}
