package com.flipperdevices.core.ktx.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author https://gist.github.com/Terenfear/a84863be501d3399889455f391eeefe5
 */
fun <T> throttleFirst(
    coroutineScope: CoroutineScope,
    skipMs: Long = 300L,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var throttleJob: Job? = null
    return { param: T ->
        if (throttleJob?.isCompleted != false) {
            throttleJob = coroutineScope.launch {
                destinationFunction(param)
                delay(skipMs)
            }
        }
    }
}
