package com.flipperdevices.bsb.wear.messenger.message


data class DecodedWearMessage<T>(
    val path: String,
    val value: T
)