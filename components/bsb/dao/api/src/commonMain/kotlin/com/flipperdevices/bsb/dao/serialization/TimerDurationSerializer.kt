package com.flipperdevices.bsb.dao.serialization

import com.flipperdevices.bsb.dao.model.TimerDuration
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Duration

/**
 * Same serializer as [kotlinx.serialization.internal.DurationSerializer]
 */
object TimerDurationSerializer : KSerializer<TimerDuration> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "TimerDuration",
        kind = PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): TimerDuration {
        val rawDate = decoder.decodeString()
        val duration = Duration.parseIsoString(rawDate)
        return TimerDuration(duration)
    }

    override fun serialize(encoder: Encoder, value: TimerDuration) {
        val duration = when (value) {
            is TimerDuration.Finite -> value.instance
            TimerDuration.Infinite -> Duration.ZERO
        }
        val rawDate = duration.toIsoString()
        encoder.encodeString(rawDate)
    }
}
