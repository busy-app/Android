package com.flipperdevices.bsb.wear.messenger.serializer

interface WearMessageSerializer<T> {
    val path: String

    fun encode(value: T): ByteArray
    fun decode(byteArray: ByteArray): T
}
