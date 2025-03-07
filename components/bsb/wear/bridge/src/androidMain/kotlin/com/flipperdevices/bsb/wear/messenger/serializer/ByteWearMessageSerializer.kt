package com.flipperdevices.bsb.wear.messenger.serializer

class ByteWearMessageSerializer(
    path: String,
) : WearMessageSerializer<Byte> by InlineWearMessageSerializer(
    path = path,
    encode = { value -> byteArrayOf(value) },
    decode = { byteArray -> byteArray.first() }
)
