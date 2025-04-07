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

class CoercedIterationBuilderTest {

    @Test
    fun GIVEN_finite_without_iterations_WHEN_build_THEN_only_work() {
        val iterationBuilder = CoercedIterationBuilder(DefaultIterationBuilder())
        val iterations = iterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Finite(15.minutes * 1),
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = false
                ),
            ),
            duration = 15.minutes * 1
        )
        val actualIterations = listOf(
            IterationData.Default(
                startOffset = 0.seconds,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes * 1
            )
        )
        assertEquals(actualIterations, iterations)
    }

    @Test
    fun GIVEN_infinite_without_iterations_WHEN_build_THEN_only_work() {
        val iterationBuilder = CoercedIterationBuilder(DefaultIterationBuilder())
        val iterations = iterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Infinite,
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = false
                ),
            ),
            duration = 15.minutes * 1
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
    fun GIVEN_finite_with_iterations_WHEN_build_THEN_work_and_long_rest() {
        val iterationBuilder = CoercedIterationBuilder(DefaultIterationBuilder())
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
                duration = 15.minutes * 1
            ),
            IterationData.Default(
                startOffset = 15.minutes * 1,
                iterationType = IterationType.Default.LONG_REST,
                duration = 15.minutes * 1
            )
        )
        assertEquals(actualIterations, iterations)
    }

    @Test
    fun GIVEN_finite_with_iterations_and_not_enough_time_WHEN_build_THEN_work_and_long_rest() {
        val iterationBuilder = CoercedIterationBuilder(DefaultIterationBuilder())
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
            duration = 16.minutes
        )
        val actualIterations = listOf(
            IterationData.Default(
                startOffset = 15.minutes * 0,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes * 1
            ),
            IterationData.Default(
                startOffset = 15.minutes * 1,
                iterationType = IterationType.Default.LONG_REST,
                duration = 15.minutes * 1
            )
        )
        assertEquals(actualIterations, iterations)
    }

    @Test
    fun GIVEN_finite_with_iterations_and_not_enough_time_and_no_autostart_WHEN_build_THEN_work_and_long_rest() {
        val iterationBuilder = CoercedIterationBuilder(DefaultIterationBuilder())
        val iterations = iterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Finite(15.minutes * 1),
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = true,
                    work = 15.minutes,
                    rest = 15.minutes,
                    longRest = 15.minutes,
                    autoStartRest = false,
                    autoStartWork = false
                ),
            ),
            duration = 16.minutes
        )
        val actualIterations = listOf(
            IterationData.Default(
                startOffset = 15.minutes * 0,
                iterationType = IterationType.Default.WORK,
                duration = 15.minutes * 1
            ),
            IterationData.Pending(
                startOffset = 15.minutes * 1,
                iterationType = IterationType.Await.WAIT_AFTER_WORK
            ),
            IterationData.Default(
                startOffset = 15.minutes * 1,
                iterationType = IterationType.Default.LONG_REST,
                duration = 15.minutes * 1
            )
        )
        assertEquals(actualIterations, iterations)
    }

    @Test
    fun GIVEN_infinite_with_iterations_WHEN_build_THEN_return_iterations() {
        val defaultIterationBuilder = DefaultIterationBuilder()
        val coercedIterationBuilder = CoercedIterationBuilder(DefaultIterationBuilder())
        val defaultIterations = defaultIterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Infinite,
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = true,
                    work = 15.minutes,
                    rest = 15.minutes,
                    longRest = 15.minutes,
                ),
            ),
            duration = 15.minutes * 1
        )
        val coercedIterations = coercedIterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Infinite,
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = true,
                    work = 15.minutes,
                    rest = 15.minutes,
                    longRest = 15.minutes,
                ),
            ),
            duration = 15.minutes * 1
        )
        assertEquals(defaultIterations, coercedIterations)
    }

    @Test
    fun GIVEN_finite_with_no_time_WHEN_build_THEN_return_iterations() {
        val defaultIterationBuilder = DefaultIterationBuilder()
        val coercedIterationBuilder = CoercedIterationBuilder(DefaultIterationBuilder())
        val defaultIterations = defaultIterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Finite(30.minutes),
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = false,
                    work = 15.minutes,
                    rest = 15.minutes,
                    longRest = 15.minutes,
                ),
            ),
            duration = 12.minutes
        )
        val coercedIterations = coercedIterationBuilder.build(
            TimerSettings(
                id = TimerSettingsId(-1L),
                totalTime = TimerDuration.Finite(30.minutes),
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = false,
                    work = 15.minutes,
                    rest = 15.minutes,
                    longRest = 15.minutes,
                ),
            ),
            duration = 12.minutes
        )
        assertEquals(defaultIterations, coercedIterations)
    }
}
