package com.flipperdevices.bsb.analytics.metric.api

import android.os.Bundle
import com.flipperdevices.bsb.analytics.metric.api.model.BEvent
import com.flipperdevices.core.di.AppGraph
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.getValue

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, MetricApi::class)
class FirebaseMetricApi : MetricApi {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    @Suppress("CyclomaticComplexMethod")
    private fun BEvent.asBundle() = when (this) {
        is BEvent.AppLaunched -> Bundle().apply {
            putLong("timestamp", timestampUtcMillis)
            putString("version", version)
        }

        is BEvent.BlockedAppAttempt -> Bundle().apply {
            putString("app_name", appName)
            putString("app_category", appCategory)
            putInt("attempt_count", attemptCount)
        }

        is BEvent.TimerAborted -> Bundle().apply {
            putLong("progress_at_abort", timePassedBeforeAbortMillis)
            putLong("abort_time", abortTimeUtcMillis)
        }

        is BEvent.TimerCompleted -> Bundle().apply {
            putSerializable("timer_config_snapshot", timerConfigSnapshot)
            putLong("competition_time", competitionTimeUtcMillis)
        }

        is BEvent.TimerPaused -> Bundle().apply {
            putLong("current_progress", timePassedMillis)
            putLong("pause_time", pauseTimeUtcMillis)
        }

        is BEvent.TimerResumed -> Bundle().apply {
            putLong("current_progress", pauseDurationMillis)
            putLong("resume_time", resumeTimeUtcMillis)
        }

        is BEvent.TimerSkipped -> Bundle().apply {
            putLong("progress_at_skip", timePassedBeforeSkipMillis)
            putLong("skip_time", skipTimeUtcMillis)
        }

        is BEvent.TimerStarted -> Bundle().apply {
            putSerializable("timer_config_snapshot", timerConfigSnapshot)
            putLong("start_time_utc", startTimeUtcMillis)
        }
    }

    override fun reportEvent(event: BEvent) {
        coroutineScope.launch {
            firebaseAnalytics.logEvent(event.key, event.asBundle())
        }
    }
}
