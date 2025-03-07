package com.flipperdevices.bsb.wear.messenger.serializer

data class DecodedWearMessage<T>(
    val path: String,
    val value: T
)
