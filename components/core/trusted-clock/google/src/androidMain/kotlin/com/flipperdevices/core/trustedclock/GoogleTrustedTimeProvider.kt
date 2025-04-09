package com.flipperdevices.core.trustedclock

import android.content.Context
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
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
    private val scope: CoroutineScope,
    private val context: Context
) : TrustedClock, LogTagProvider {
    override val TAG: String = "GoogleTrustedTimeProvider"

    private val trustedClientFlow = callbackFlow {
        val clientTask = TrustedTime.createClient(context)
        val client = runCatching { clientTask.await() }
            .onFailure { error(it) { "#trustedClientFlow could not get client" } }
            .getOrNull()
        send(client)
        awaitClose {
            client?.dispose()
        }
    }.stateIn(scope, SharingStarted.Eagerly, null)

    override fun now(): Instant {
        val trustedInstant = trustedClientFlow.value
            ?.computeCurrentInstant()
            ?.toKotlinInstant()
        return trustedInstant ?: Clock.System.now()
    }
}
