@file:Suppress("LongMethod")

package com.flipperdevices.bsb.timer.statefactory.iteration.impl

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.statefactory.iteration.model.IterationData
import com.flipperdevices.bsb.timer.statefactory.iteration.model.IterationType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DefaultIterationBuilderTest {

    @Test
    fun GIVEN_finite_without_iterations_WHEN_build_THEN_only_work() {
        val iterationBuilder = DefaultIterationBuilder()
        val iterations = iterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Finite(90.minutes),
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = false
                ),
            ),
            duration = 90.minutes
        )
        val actualIterations = listOf(
            IterationData.Default(
                startOffset = 0.seconds,
                iterationType = IterationType.Default.WORK,
                duration = 90.minutes
            )
        )
        assertEquals(actualIterations, iterations)
    }

    @Test
    fun GIVEN_infinite_without_iterations_WHEN_build_THEN_only_work() {
        val iterationBuilder = DefaultIterationBuilder()
        val iterations = iterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Infinite,
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = false
                ),
            ),
            duration = 90.minutes
        )

        val actualIterations = listOf(
            IterationData.Infinite(
                startOffset = 0.seconds,
                iterationType = IterationType.Default.WORK,
            )
        )
        assertEquals(actualIterations, iterations)
    }

    @Test
    fun GIVEN_finite_with_not_enough_time_for_last_long_rest_WHEN_build_THEN_without_last_long_rest() {
        val iterationBuilder = DefaultIterationBuilder()
        val iterations = iterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Finite(15.minutes * 11),
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = true,
                    work = 15.minutes,
                    rest = 15.minutes,
                    longRest = 15.minutes
                ),
            ),
            duration = 15.minutes * 11
        )

        val actualIterations = listOf(
            IterationData.Default(
                startOffset = 15.minutes * 0,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 1,
                iterationType = IterationType.Default.REST,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 2,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 3,
                iterationType = IterationType.Default.REST,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 4,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 5,
                iterationType = IterationType.Default.LONG_REST,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 6,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 7,
                iterationType = IterationType.Default.REST,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 8,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 9,
                iterationType = IterationType.Default.REST,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 10,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
        )
        assertEquals(actualIterations, iterations)
    }

    @Test
    fun GIVEN_finite_with_enough_time_for_only_first_WHEN_build_THEN_only_work() {
        val iterationBuilder = DefaultIterationBuilder()
        val iterations = iterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Finite(15.minutes * 1),
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = true,
                    work = 15.minutes,
                    rest = 15.minutes,
                    longRest = 15.minutes
                ),
            ),
            duration = 15.minutes * 1
        )

        val actualIterations = listOf(
            IterationData.Default(
                startOffset = 15.minutes * 0,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
        )
        assertEquals(actualIterations, iterations)
    }

    @Test
    fun GIVEN_finite_with_little_more_time_for_second_WHEN_build_THEN_work_and_rest() {
        val iterationBuilder = DefaultIterationBuilder()
        val iterations = iterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Finite(16.minutes),
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = true,
                    work = 15.minutes,
                    rest = 15.minutes,
                    longRest = 15.minutes
                ),
            ),
            duration = 16.minutes
        )

        val actualIterations = listOf(
            IterationData.Default(
                startOffset = 15.minutes * 0,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 1,
                iterationType = IterationType.Default.REST,
                duration = 15.minutes
            ),
        )
        assertEquals(actualIterations, iterations)
    }

    @Test
    fun GIVEN_infinite_with_little_more_time_for_second_WHEN_build_THEN_work_and_rest() {
        val iterationBuilder = DefaultIterationBuilder()
        val iterations = iterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Infinite,
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = true,
                    work = 15.minutes,
                    rest = 15.minutes,
                    longRest = 15.minutes
                ),
            ),
            duration = 16.minutes
        )

        val actualIterations = listOf(
            IterationData.Default(
                startOffset = 15.minutes * 0,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
            IterationData.Default(
                startOffset = 15.minutes * 1,
                iterationType = IterationType.Default.REST,
                duration = 15.minutes
            ),
        )
        assertEquals(actualIterations, iterations)
    }

    @Test
    fun GIVEN_infinite_with_little_more_time_for_third_WHEN_build_THEN_work_await_rest_await_work() {
        val iterationBuilder = DefaultIterationBuilder()
        val iterations = iterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Infinite,
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = true,
                    work = 15.minutes,
                    rest = 15.minutes,
                    longRest = 15.minutes,
                    autoStartRest = false,
                    autoStartWork = false
                ),
            ),
            duration = 31.minutes
        )

        val actualIterations = listOf(
            IterationData.Default(
                startOffset = 15.minutes * 0,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
            IterationData.Pending(
                startOffset = 15.minutes * 1,
                iterationType = IterationType.Await.WAIT_AFTER_WORK
            ),
            IterationData.Default(
                startOffset = 15.minutes * 1,
                iterationType = IterationType.Default.REST,
                duration = 15.minutes
            ),
            IterationData.Pending(
                startOffset = 15.minutes * 2,
                iterationType = IterationType.Await.WAIT_AFTER_REST
            ),
            IterationData.Default(
                startOffset = 15.minutes * 2,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
        )
        assertEquals(actualIterations, iterations)
    }

    @Test
    fun GIVEN_infinite_with_little_more_time_for_work_after_long_rest_WHEN_build_THEN_work_await_rest_await_work() {
        val iterationBuilder = DefaultIterationBuilder()
        val iterations = iterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Infinite,
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = true,
                    work = 15.minutes,
                    rest = 15.minutes,
                    longRest = 15.minutes,
                    autoStartRest = false,
                    autoStartWork = false
                ),
            ),
            duration = 15.minutes * 6 + 1.minutes
        )

        val actualIterations = listOf(
            IterationData.Default(
                startOffset = 15.minutes * 0,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
            IterationData.Pending(
                startOffset = 15.minutes * 1,
                iterationType = IterationType.Await.WAIT_AFTER_WORK
            ),
            IterationData.Default(
                startOffset = 15.minutes * 1,
                iterationType = IterationType.Default.REST,
                duration = 15.minutes
            ),
            IterationData.Pending(
                startOffset = 15.minutes * 2,
                iterationType = IterationType.Await.WAIT_AFTER_REST
            ),
            IterationData.Default(
                startOffset = 15.minutes * 2,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
            IterationData.Pending(
                startOffset = 15.minutes * 3,
                iterationType = IterationType.Await.WAIT_AFTER_WORK
            ),
            IterationData.Default(
                startOffset = 15.minutes * 3,
                iterationType = IterationType.Default.REST,
                duration = 15.minutes
            ),
            IterationData.Pending(
                startOffset = 15.minutes * 4,
                iterationType = IterationType.Await.WAIT_AFTER_REST
            ),
            IterationData.Default(
                startOffset = 15.minutes * 4,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
            IterationData.Pending(
                startOffset = 15.minutes * 5,
                iterationType = IterationType.Await.WAIT_AFTER_WORK
            ),
            IterationData.Default(
                startOffset = 15.minutes * 5,
                iterationType = IterationType.Default.LONG_REST,
                duration = 15.minutes
            ),
            IterationData.Pending(
                startOffset = 15.minutes * 6,
                iterationType = IterationType.Await.WAIT_AFTER_REST
            ),
            IterationData.Default(
                startOffset = 15.minutes * 6,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes
            ),
        )
        assertEquals(actualIterations, iterations)
    }
}
