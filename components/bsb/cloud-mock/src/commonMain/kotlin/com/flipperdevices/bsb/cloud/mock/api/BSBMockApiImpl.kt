package com.flipperdevices.bsb.cloud.mock.api

import com.flipperdevices.bsb.cloud.model.response.BSBApiUserObject
import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.core.di.AppGraph
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, BSBMockApi::class)
class BSBMockApiImpl(
    private val httpClient: HttpClient,
    private val krateApi: KrateApi
) : BSBMockApi {
    override suspend fun auth(): Result<BSBApiUserObject> {
        return runCatching {
            httpClient.get {
                url("${BSBMockApi.BASE_URL}/v0/auth")
                header("Authorization-Firebase", krateApi.firebaseTokenKrate.loadAndGet())
            }.body<BSBApiUserObject>()
        }.onFailure { it.printStackTrace() }
    }

    override suspend fun saveTimer(timestamp: TimerTimestamp): Result<TimerTimestamp> {
        return runCatching {
            httpClient.post {
                url("${BSBMockApi.BASE_URL}/v0/timer/remember")
                setBody(timestamp)
                header("Authorization-Firebase", krateApi.firebaseTokenKrate.loadAndGet())
            }.body<TimerTimestamp>()
        }.onFailure { it.printStackTrace() }
    }

    override suspend fun getTimer(): Result<TimerTimestamp> {
        return runCatching {
            httpClient.get {
                url("${BSBMockApi.BASE_URL}/v0/timer/read")
                header("Authorization-Firebase", krateApi.firebaseTokenKrate.loadAndGet())
            }.body<TimerTimestamp>()
        }.onFailure { it.printStackTrace() }
    }
}
