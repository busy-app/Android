package com.flipperdevices.bsb.wear.messenger.serializer

class ByteWearMessageSerializer(
    path: String,
) : WearMessageSerializer<Byte> by InlineWearMessageSerializer(
    path = path,
    encode = { value ->
        byteArrayOf(overrideValue ?: value)
    },
    decode = { byteArray ->
        overrideValue ?: byteArray.first()
    }
)
