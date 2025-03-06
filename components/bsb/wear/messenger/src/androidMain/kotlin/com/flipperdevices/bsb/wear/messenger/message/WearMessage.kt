package com.flipperdevices.bsb.wear.messenger.message

interface WearMessage<T> {
    val path: String

    fun encode(value: T): ByteArray
    fun decode(byteArray: ByteArray): T
}