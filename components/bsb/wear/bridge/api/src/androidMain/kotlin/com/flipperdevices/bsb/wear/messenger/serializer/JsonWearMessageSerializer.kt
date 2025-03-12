package com.flipperdevices.bsb.wear.messenger.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class JsonWearMessageSerializer<T>(
    path: String,
    serializer: KSerializer<T>,
    json: Json = DEFAULT_JSON
) : WearMessageSerializer<T> by InlineWearMessageSerializer(
    path = path,
    encode = { value ->
        val string = json.encodeToString(serializer, value)
        string.toByteArray()
    },
    decode = { byteArray ->
        val string = byteArray.decodeToString()
        json.decodeFromString(serializer, string)
    }
) {
    companion object {
        val DEFAULT_JSON = Json {
            prettyPrint = false
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
}

@Suppress("FunctionNaming")
inline fun <reified T> JsonWearMessage(
    json: Json = JsonWearMessageSerializer.DEFAULT_JSON,
    path: String
): JsonWearMessageSerializer<T> = JsonWearMessageSerializer(
    serializer = json.serializersModule.serializer<T>(),
    path = path,
    json = json,
)
