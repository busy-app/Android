package com.flipperdevices.bsb.wear.messenger.serializer

class InlineWearMessageSerializer<T>(
    override val path: String,
    private val encode: (T) -> ByteArray,
    private val decode: (ByteArray) -> T
) : WearMessageSerializer<T> {
    override fun encode(value: T): ByteArray = this.encode.invoke(value)

    override fun decode(byteArray: ByteArray): T = this.decode.invoke(byteArray)
}
