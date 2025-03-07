package com.flipperdevices.bsb.wear.messenger.di

import android.content.Context
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.core.di.AppGraph
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.data.WearDataLayerRegistry
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@OptIn(ExperimentalHorologistApi::class)
@ContributesTo(AppGraph::class)
interface WearDataLayerModule {

    @Provides
    @SingleIn(AppGraph::class)
    fun provideWearDataLayerRegistry(
        context: Context,
        coroutineScope: CoroutineScope
    ): WearDataLayerRegistry {
        return WearDataLayerRegistry.fromContext(
            application = context,
            coroutineScope = coroutineScope
        )
    }

    val wearMessageConsumer: WearMessageConsumer
    val wearMessageProducer: WearMessageProducer
}