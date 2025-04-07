package com.flipperdevices.bsb.timer.statefactory.iteration

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.statefactory.TimerStateFactoryImpl
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.minutes

class TimerStateFactoryTest {

    @Test
    fun GIVEN_not_started_WHEN_build_THEN_not_started() {
        val timerStateFactory = TimerStateFactoryImpl()
        val actual = timerStateFactory.create(TimerTimestamp.Pending.Finished)
        assertIs<ControlledTimerState.NotStarted>(actual)
    }

    @Test
    fun GIVEN_not_confirmed_a_lot_time_passed_WHEN_build_THEN_still_work() {
        val timerStateFactory = TimerStateFactoryImpl()
        val actual = timerStateFactory.create(
            TimerTimestamp.Running(
                lastSync = Instant.DISTANT_PAST,
                start = Clock.System.now().minus(15.minutes * 14),
                confirmNextStepClick = Instant.DISTANT_FUTURE,
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
        val timerStateFactory = TimerStateFactoryImpl()
        val actual = timerStateFactory.create(
            TimerTimestamp.Running(
                lastSync = Instant.DISTANT_PAST,
                start = Clock.System.now().minus(15.minutes * 14),
                confirmNextStepClick = Instant.DISTANT_PAST,
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
        val timerStateFactory = TimerStateFactoryImpl()
        val actual = timerStateFactory.create(
            TimerTimestamp.Running(
                lastSync = Instant.DISTANT_PAST,
                start = Clock.System.now().minus(15.minutes * 14),
                confirmNextStepClick = Instant.DISTANT_FUTURE,
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
