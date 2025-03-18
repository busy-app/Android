package com.flipperdevices.bsb.analytics.metric.api.model

import com.flipperdevices.core.buildkonfig.BuildKonfig
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName

sealed class BEvent(val key: String) {
    /**
     * User launched an app
     */
    class AppLaunched : BEvent("app_launched") {
        @SerialName("timestamp")
        val timestampUtcMillis: Long = Clock.System.now().toEpochMilliseconds()

        @SerialName("version")
        val version: String = BuildKonfig.VERSION
    }

    /**
     * User started a timer
     */
    data class TimerStarted(
        @SerialName("timer_config_snapshot")
        val timerConfigSnapshot: TimerConfigSnapshot
    ) : BEvent("timer_started") {
        @SerialName("start_time_utc")
        val startTimeUtcMillis: Long = Clock.System.now().toEpochMilliseconds()
    }

    /**
     * User fully complete timer and navigated to finish screen
     * @param competitionTimeUtcMillis utc time when timer finished
     */
    data class TimerCompleted(
        @SerialName("timer_config_snapshot")
        val timerConfigSnapshot: TimerConfigSnapshot
    ) : BEvent("timer_completed") {
        @SerialName("competition_time")
        val competitionTimeUtcMillis: Long = Clock.System.now().toEpochMilliseconds()
    }

    /**
     * User pause timer
     * @param timePassedMillis time passed from start until pause
     * @param pauseTimeUtcMillis utc time when pause started
     */
    data class TimerPaused(
        @SerialName("current_progress")
        val timePassedMillis: Long
    ) : BEvent("timer_paused") {
        @SerialName("pause_time")
        val pauseTimeUtcMillis: Long = Clock.System.now().toEpochMilliseconds()
    }

    /**
     * User resume timer
     * @param pauseDurationMillis how long timer was in pause
     * @param resumeTimeUtcMillis utc time when timer was resumed
     */
    data class TimerResumed(
        @SerialName("current_progress")
        val pauseDurationMillis: Long
    ) : BEvent("timer_resumed") {
        @SerialName("resume_time")
        val resumeTimeUtcMillis: Long = Clock.System.now().toEpochMilliseconds()
    }

    /**
     * User skip current timer iteration
     * @param timePassedBeforeSkipMillis how much time passed before user pressed skip
     * @param skipTimeUtcMillis utc time when user pressed skip
     */
    data class TimerSkipped(
        @SerialName("progress_at_skip")
        val timePassedBeforeSkipMillis: Long
    ) : BEvent("timer_skipped") {
        @SerialName("skip_time")
        val skipTimeUtcMillis: Long = Clock.System.now().toEpochMilliseconds()
    }

    /**
     * User press stop button and cancel timer
     * @param timePassedBeforeAbortMillis how much time passed before user pressed stop
     * @param abortTimeUtcMillis utc time when user pressed stop
     */
    data class TimerAborted(
        @SerialName("progress_at_abort")
        val timePassedBeforeAbortMillis: Long
    ) : BEvent("timer_aborted") {
        @SerialName("abort_time")
        val abortTimeUtcMillis: Long = Clock.System.now().toEpochMilliseconds()
    }

    /**
     * User opened blocked application
     */
    data class BlockedAppAttempt(
        @SerialName("app_name")
        val appName: String,
        @SerialName("app_category")
        val appCategory: String,
        @SerialName("attempt_count")
        val attemptCount: Int
    ) : BEvent("blocked_app_attempt") {
        @SerialName("attempt_time")
        val attemptTimeUtcMillis: Long = Clock.System.now().toEpochMilliseconds()
    }
}
