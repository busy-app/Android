package com.flipperdevices.bsb.wear.messenger.message

import android.util.Log
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class JsonWearMessage<T>(
    path: String,
    serializer: KSerializer<T>,
    json: Json = DEFAULT_JSON
) : WearMessage<T> by InlineWearMessage(
    path = path,
    encode = { value ->
        val string = json.encodeToString(serializer, value)
        Log.d("InlineWearMessage", "InlineWearMessage->encode: $string")
        string.toByteArray()
    },
    decode = { byteArray ->
        val string = byteArray.decodeToString()
        Log.d("InlineWearMessage", "InlineWearMessage->decode: $string")
        json.decodeFromString(serializer, string)
    }
) {
    companion object {
        internal val DEFAULT_JSON = Json {
            prettyPrint = false
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
}

@Suppress("FunctionNaming")
inline fun <reified T> JsonWearMessage(
    json: Json,
    path: String
): WearMessage<T> = JsonWearMessage(
    serializer = json.serializersModule.serializer<T>(),
    path = path,
    json = json,
)