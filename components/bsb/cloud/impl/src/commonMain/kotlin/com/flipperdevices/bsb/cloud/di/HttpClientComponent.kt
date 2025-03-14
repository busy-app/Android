package com.flipperdevices.bsb.cloud.di

import com.flipperdevices.bsb.cloud.di.http.BSBApiErrorHandlerPlugin
import com.flipperdevices.bsb.cloud.di.http.BSBAuthPlugin
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.TaggedLogger
import com.flipperdevices.core.log.verbose
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

private val ktorTimber = TaggedLogger("Ktor")

@ContributesTo(AppGraph::class)
interface HttpClientComponent {
    @Provides
    fun provideHttpClient(preferenceApi: PreferenceApi): HttpClient = getHttpClient(preferenceApi)
}

fun getHttpClient(preferenceApi: PreferenceApi) = HttpClient(httpEngine()) {
    install(BSBApiErrorHandlerPlugin)

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        )
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                ktorTimber.verbose { message }
            }
        }
        level = LogLevel.ALL
    }

    install(BSBAuthPlugin) {
        this.preferenceApi = preferenceApi
    }

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}

expect fun httpEngine(): HttpClientEngine
