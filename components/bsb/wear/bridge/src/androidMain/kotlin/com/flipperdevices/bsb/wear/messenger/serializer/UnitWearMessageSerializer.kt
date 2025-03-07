package com.flipperdevices.bsb.wear.messenger.serializer

class UnitWearMessageSerializer(
    path: String,
) : WearMessageSerializer<Unit> by InlineWearMessageSerializer(
    path = path,
    encode = { byteArrayOf() },
    decode = { Unit }
)
