package com.flipperdevices.bsb.dao.serialization

import com.flipperdevices.bsb.dao.model.TimerDuration
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Duration.Companion.milliseconds

/**
 * Same serializer as [kotlinx.serialization.internal.DurationSerializer]
 */
object TimerDurationSerializer : KSerializer<TimerDuration> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "TimerDuration",
        kind = PrimitiveKind.LONG
    )

    override fun deserialize(decoder: Decoder): TimerDuration {
        val millis = decoder.decodeLong()
        return when (millis) {
            -1L -> TimerDuration.Infinite
            else -> TimerDuration.Finite(millis.milliseconds)
        }
    }

    override fun serialize(encoder: Encoder, value: TimerDuration) {
        val millis = when (value) {
            is TimerDuration.Finite -> value.instance.inWholeMilliseconds
            TimerDuration.Infinite -> -1L
        }
        encoder.encodeLong(millis)
    }
}
