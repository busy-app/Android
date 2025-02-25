package com.flipperdevices.bsb.timer.background.api.util

import com.flipperdevices.bsb.preference.model.TimerSettings
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.time.Duration.Companion.minutes

class ControlledTimerStateFactoryTest {

    @Test
    fun `GIVEN_standart_iterations_WHEN_build_iterations_THEN_ok`() {
        TimerSettings(
            totalTime = 95.minutes,
            intervalsSettings = TimerSettings.IntervalsSettings(
                isEnabled = true,
                work = 25.minutes,
                rest = 5.minutes,
                longRest = 10.minutes
            )
        ).let { settings ->
            val list = settings.buildIterationList().map(IterationData::iterationType)
            assertContentEquals(
                expected = listOf(
                    IterationType.WORK,
                    IterationType.REST,
                    IterationType.WORK,
                    IterationType.REST,
                    IterationType.WORK,
                    IterationType.LONG_REST
                ),
                actual = list
            )
        }
    }

    @Test
    fun `GIVEN_too_long_rest_WHEN_build_iterations_THEN_last_long_rest`() {
        TimerSettings(
            totalTime = 60.minutes,
            intervalsSettings = TimerSettings.IntervalsSettings(
                isEnabled = true,
                work = 25.minutes,
                rest = 256.minutes,
                longRest = 10.minutes
            )
        ).let { settings ->
            val list = settings.buildIterationList().map(IterationData::iterationType)
            assertContentEquals(
                expected = listOf(
                    IterationType.WORK,
                    IterationType.LONG_REST,
                ),
                actual = list
            )
        }
    }

    @Test
    fun `GIVEN_too_long_long_rest_short_rest_WHEN_build_iterations_THEN_last_long_rest`() {
        TimerSettings(
            totalTime = 60.minutes,
            intervalsSettings = TimerSettings.IntervalsSettings(
                isEnabled = true,
                work = 25.minutes,
                rest = 3.minutes,
                longRest = 256.minutes
            )
        ).let { settings ->
            val list = settings.buildIterationList()
                .also { println(it) }
                .map(IterationData::iterationType)
            assertContentEquals(
                expected = listOf(
                    IterationType.WORK,
                    IterationType.REST,
                    IterationType.WORK,
                    IterationType.LONG_REST,
                ),
                actual = list
            )
        }
    }

    @Test
    fun `GIVEN_too_short_long_rest_WHEN_long_rest_is_last_THEN_add_diff_time_to_long_rest`() {
        TimerSettings(
            totalTime = 105.minutes,
            intervalsSettings = TimerSettings.IntervalsSettings(
                isEnabled = true,
                work = 25.minutes,
                rest = 5.minutes,
                longRest = 15.minutes
            )
        ).let { settings ->
            val list = settings.buildIterationList()
                .also { println(it) }
                .map(IterationData::iterationType)
            assertContentEquals(
                expected = listOf(
                    IterationType.WORK,
                    IterationType.REST,
                    IterationType.WORK,
                    IterationType.REST,
                    IterationType.WORK,
                    IterationType.LONG_REST,
                ),
                actual = list
            )
        }
    }

    @Test
    fun `GIVEN_same_rests_WHEN_no_time_left_THEN_last_long_rest`() {
        TimerSettings(
            totalTime = 60.minutes,
            intervalsSettings = TimerSettings.IntervalsSettings(
                isEnabled = true,
                work = 50.minutes,
                rest = 10.minutes,
                longRest = 10.minutes
            )
        ).let { settings ->
            val list = settings.buildIterationList().map(IterationData::iterationType)
            assertContentEquals(
                expected = listOf(
                    IterationType.WORK,
                    IterationType.LONG_REST,
                ),
                actual = list
            )
        }
    }

    @Test
    fun `GIVEN_only_work_WHEN_build_iterations_THEN_ok`() {
        TimerSettings(intervalsSettings = TimerSettings.IntervalsSettings(isEnabled = false)).let { settings ->
            val list = settings.buildIterationList().map(IterationData::iterationType)
            assertContentEquals(listOf(IterationType.WORK), list)
        }
    }
}
