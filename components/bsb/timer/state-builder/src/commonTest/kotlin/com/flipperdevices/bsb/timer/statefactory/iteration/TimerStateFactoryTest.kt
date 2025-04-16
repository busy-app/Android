package com.flipperdevices.bsb.timer.statefactory.iteration

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.statefactory.TimerStateFactoryImpl
import com.flipperdevices.core.trustedclock.TrustedClock
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.minutes

class TimerStateFactoryTest {

    private val timeProvider: TrustedClock = object : TrustedClock {
        override fun initialize() = Unit
        override fun now() = Instant.fromEpochSeconds(12345678)
    }

    @Test
    fun GIVEN_not_started_WHEN_build_THEN_not_started() {
        val timerStateFactory = TimerStateFactoryImpl(timeProvider)
        val actual = timerStateFactory.create(
            TimerTimestamp.Pending.Finished(
                Instant.fromEpochSeconds(12345678)
            )
        )
        assertIs<ControlledTimerState.NotStarted>(actual)
    }

    @Test
    fun GIVEN_not_confirmed_a_lot_time_passed_WHEN_build_THEN_still_work() {
        val timerStateFactory = TimerStateFactoryImpl(timeProvider)
        val actual = timerStateFactory.create(
            TimerTimestamp.Running(
                lastSync = Instant.fromEpochSeconds(0),
                start = timeProvider.now().minus(15.minutes * 15),
                noOffsetStart = timeProvider.now(),
                confirmNextStepClick = Instant.fromEpochSeconds(Long.MAX_VALUE),
                settings = TimerSettings(
                    id = TimerSettingsId(-1L),
                    totalTime = TimerDuration.Infinite,
                    intervalsSettings = TimerSettings.IntervalsSettings(
                        isEnabled = true,
                        work = 15.minutes,
                        rest = 15.minutes,
                        longRest = 15.minutes,
                        autoStartRest = false,
                        autoStartWork = false
                    )
                )
            )
        )
        assertIs<ControlledTimerState.InProgress.Running.Work>(actual)
    }

    @Test
    fun GIVEN_confirmed_and_time_passed_WHEN_build_THEN_still_await() {
        val timerStateFactory = TimerStateFactoryImpl(timeProvider)
        val actual = timerStateFactory.create(
            TimerTimestamp.Running(
                lastSync = Instant.fromEpochSeconds(0),
                start = timeProvider.now().minus(15.minutes * 14),
                noOffsetStart = timeProvider.now(),
                confirmNextStepClick = Instant.fromEpochSeconds(0),
                settings = TimerSettings(
                    id = TimerSettingsId(-1L),
                    totalTime = TimerDuration.Infinite,
                    intervalsSettings = TimerSettings.IntervalsSettings(
                        isEnabled = true,
                        work = 15.minutes,
                        rest = 15.minutes,
                        longRest = 15.minutes,
                        autoStartRest = false,
                        autoStartWork = false
                    )
                )
            )
        )
        assertIs<ControlledTimerState.InProgress.Await>(actual)
    }

    @Test
    fun GIVEN_all_passed_WHEN_build_THEN_finished() {
        val timerStateFactory = TimerStateFactoryImpl(timeProvider)
        val actual = timerStateFactory.create(
            TimerTimestamp.Running(
                lastSync = Instant.fromEpochSeconds(0),
                start = timeProvider.now().minus(15.minutes * 14),
                noOffsetStart = timeProvider.now(),
                confirmNextStepClick = Instant.fromEpochSeconds(Long.MAX_VALUE),
                settings = TimerSettings(
                    id = TimerSettingsId(-1L),
                    totalTime = TimerDuration.Finite(15.minutes * 10),
                    intervalsSettings = TimerSettings.IntervalsSettings(
                        isEnabled = true,
                        work = 15.minutes,
                        rest = 15.minutes,
                        longRest = 15.minutes,
                        autoStartRest = false,
                        autoStartWork = false
                    )
                )
            )
        )
        assertIs<ControlledTimerState.Finished>(actual)
    }
}
