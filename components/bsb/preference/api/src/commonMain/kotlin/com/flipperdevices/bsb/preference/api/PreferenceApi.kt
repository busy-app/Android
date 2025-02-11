package com.flipperdevices.bsb.preference.api

import com.flipperdevices.bsb.preference.model.SettingsEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer

interface PreferenceApi {
    fun getFlowString(key: SettingsEnum, default: String): Flow<String>
    fun getFlowBoolean(key: SettingsEnum, default: Boolean): Flow<Boolean>
    fun <T : Any?> getFlowSerializable(
        serializer: DeserializationStrategy<T>,
        key: SettingsEnum,
        default: T
    ): Flow<T>

    fun getString(key: SettingsEnum, default: String?): String?
    fun getBoolean(key: SettingsEnum, default: Boolean?): Boolean?
    fun <T : Any?> getSerializable(
        serializer: DeserializationStrategy<T>,
        key: SettingsEnum,
        default: T
    ): T

    fun setString(key: SettingsEnum, value: String)
    fun setBoolean(key: SettingsEnum, value: Boolean)
    fun <T> setSerializable(
        serializer: SerializationStrategy<T>,
        key: SettingsEnum,
        value: T
    )
}

inline fun <reified T : Any?> PreferenceApi.get(key: SettingsEnum, default: T): T =
    when (T::class) {
        Int::class,
        Long::class,
        Float::class,
        Double::class -> throw NotImplementedError()

        String::class -> getString(key, default as String) as T
        Boolean::class -> getBoolean(key, default as Boolean) as T
        else -> getSerializable(serializer<T>(), key, default)
    }

inline fun <reified T : Any?> PreferenceApi.getFlow(key: SettingsEnum, default: T): Flow<T> =
    when (T::class) {
        Int::class,
        Long::class,
        Float::class,
        Double::class -> throw NotImplementedError()

        String::class -> getFlowString(key, default as String).map { it as T }
        Boolean::class -> getFlowBoolean(key, default as Boolean).map { it as T }
        else -> getFlowSerializable(serializer<T>(), key, default)
    }

inline fun <reified T : Any?> PreferenceApi.set(key: SettingsEnum, value: T) =
    when (T::class) {
        Int::class,
        Long::class,
        Float::class,
        Double::class -> throw NotImplementedError()

        String::class -> setString(key, value as String)
        Boolean::class -> setBoolean(key, value as Boolean)
        else -> setSerializable(serializer<T>(), key, value)
    }
