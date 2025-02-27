package com.flipperdevices.bsb.timer.focusdisplay.api

import android.view.WindowManager
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.flipperdevices.core.activityholder.CurrentActivityHolder
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AndroidFocusDisplayDecomposeComponentImpl(
    @Assisted lifecycle: Lifecycle
) : FocusDisplayDecomposeComponent(lifecycle) {

    private fun enableScreenOn() {
        CurrentActivityHolder
            .getCurrentActivity()
            ?.window
            ?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun disableScreenOn() {
        CurrentActivityHolder
            .getCurrentActivity()
            ?.window
            ?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onResume() {
        enableScreenOn()
    }

    override fun onPause() {
        disableScreenOn()
    }

    @Inject
    @ContributesBinding(AppGraph::class, FocusDisplayDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            lifecycle: Lifecycle
        ) -> AndroidFocusDisplayDecomposeComponentImpl
    ) : FocusDisplayDecomposeComponent.Factory {
        override fun invoke(
            lifecycle: Lifecycle
        ) = factory(lifecycle)
    }
}
