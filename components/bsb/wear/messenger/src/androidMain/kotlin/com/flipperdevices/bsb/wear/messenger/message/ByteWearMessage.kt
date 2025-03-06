package com.flipperdevices.bsb.wear.messenger.message

import android.util.Log
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class ByteWearMessage(path: String) : WearMessage<Byte> by InlineWearMessage(
    path = path,
    encode = { value ->
        byteArrayOf(value)
    },
    decode = { byteArray ->
        byteArray.first()
    }
)
