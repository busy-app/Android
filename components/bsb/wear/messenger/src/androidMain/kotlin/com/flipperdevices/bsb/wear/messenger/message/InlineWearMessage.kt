package com.flipperdevices.bsb.wear.messenger.message

class InlineWearMessage<T>(
    override val path: String,
    private val encode: (T) -> ByteArray,
    private val decode: (ByteArray) -> T
) : WearMessage<T> {
    override fun encode(value: T): ByteArray = this.encode.invoke(value)

    override fun decode(byteArray: ByteArray): T = this.decode.invoke(byteArray)
}