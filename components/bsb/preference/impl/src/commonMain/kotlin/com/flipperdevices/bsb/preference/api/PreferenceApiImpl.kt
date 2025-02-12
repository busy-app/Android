package com.flipperdevices.bsb.preference.api

import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.core.di.AppGraph
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, PreferenceApi::class)
class PreferenceApiImpl(
    private val observableSettings: ObservableSettings
) : PreferenceApi {
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = false
    }
    private val flowSettings = observableSettings.toFlowSettings()

    override fun getFlowString(
        key: SettingsEnum,
        default: String
    ) = flowSettings.getStringFlow(key.key, default)

    override fun getFlowBoolean(
        key: SettingsEnum,
        default: Boolean
    ) = flowSettings.getBooleanFlow(key.key, default)

    override fun <T> getFlowSerializable(
        serializer: DeserializationStrategy<T>,
        key: SettingsEnum,
        default: T
    ): Flow<T> {
        return flowSettings.getStringOrNullFlow(key.key)
            .map {
                if (it.isNullOrBlank()) {
                    default
                } else {
                    json.decodeFromString(serializer, it)
                }
            }
    }

    override fun getString(key: SettingsEnum, default: String?): String? {
        return observableSettings.getStringOrNull(key.key) ?: default
    }

    override fun getBoolean(key: SettingsEnum, default: Boolean?): Boolean? {
        return observableSettings.getBooleanOrNull(key.key) ?: default
    }

    override fun <T : Any?> getSerializable(
        serializer: DeserializationStrategy<T>,
        key: SettingsEnum,
        default: T
    ): T {
        val result = observableSettings.getStringOrNull(key.key)
        if (result.isNullOrBlank()) {
            return default
        }
        return json.decodeFromString(serializer, result)
    }

    override fun <T> setSerializable(
        serializer: SerializationStrategy<T>,
        key: SettingsEnum,
        value: T
    ) {
        observableSettings.putString(key.key, json.encodeToString(serializer, value))
    }

    override fun setBoolean(key: SettingsEnum, value: Boolean) {
        observableSettings.putBoolean(key.key, value)
    }

    override fun setString(key: SettingsEnum, value: String) {
        observableSettings.putString(key.key, value)
    }
}
