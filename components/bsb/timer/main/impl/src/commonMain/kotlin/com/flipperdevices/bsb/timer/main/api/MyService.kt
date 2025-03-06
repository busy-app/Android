package com.flipperdevices.bsb.timer.main.api

import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

interface MyService {
    fun start(scope: CoroutineScope)
}
