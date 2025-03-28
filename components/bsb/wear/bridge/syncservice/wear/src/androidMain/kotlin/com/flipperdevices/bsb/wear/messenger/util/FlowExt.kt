package com.flipperdevices.bsb.wear.messenger.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.ArrayList

/**
 * Example of usage:
 * ```
 * flowOf(1, 2, 3, 4, 5, 6, 7)
 *     .overflowChunked(2) // [1, 2], [2, 3], [3, 4], [4, 5]
 * ```
 */
internal fun <T> Flow<T>.overflowChunked(size: Int): Flow<List<T>> {
    require(size >= 1) { "Expected positive chunk size, but got $size" }
    return flow {
        val buffer = ArrayList<T?>(size).also { null }
        collect { value ->
            if (buffer.size != size) {
                buffer.add(value)
            } else {
                for (i in 1 until size) {
                    buffer[i - 1] = buffer[i]
                }
                buffer[size - 1] = value
            }
            val safeBuffer = buffer.filterNotNull()
            if (safeBuffer.size == size) {
                emit(safeBuffer)
            }
        }
        val nonNullResult = buffer.filterNotNull()
        if (nonNullResult.size == size) {
            emit(nonNullResult)
        }
    }
}
