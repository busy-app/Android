package com.flipperdevices.bsb.cloud.mock.api

import com.flipperdevices.bsb.cloud.model.response.BSBApiUserObject
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.core.di.AppGraph
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, BSBMockApi::class)
class BSBMockApiImpl(
    private val httpClient: HttpClient,
) : BSBMockApi {
    override suspend fun auth(): Result<BSBApiUserObject> {
        return runCatching {
            httpClient.get {
                url("${BSBMockApi.BASE_URL}/v0/auth")
            }.body<BSBApiUserObject>()
        }
    }

    override suspend fun saveTimer(timestamp: TimerTimestamp): Result<TimerTimestamp> {
        return runCatching {
            httpClient.post {
                url("${BSBMockApi.BASE_URL}/v0/auth")
                setBody(timestamp)
            }.body<TimerTimestamp>()
        }
    }

    override suspend fun getTimer(): Result<TimerTimestamp> {
        return runCatching {
            httpClient.get {
                url("${BSBMockApi.BASE_URL}/v0/auth")
            }.body<TimerTimestamp>()
        }
    }
}
