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

    override fun onResume() {
        CurrentActivityHolder
            .getCurrentActivity()
            ?.window
            ?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        CurrentActivityHolder
            .getCurrentActivity()
            ?.window
            ?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
