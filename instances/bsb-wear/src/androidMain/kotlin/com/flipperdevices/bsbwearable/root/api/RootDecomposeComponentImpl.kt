package com.flipperdevices.bsbwearable.root.api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.flipperdevices.bsbwearable.active.api.ActiveTimerScreenDecomposeComponent
import com.flipperdevices.bsbwearable.composable.SwipeToDismissBox
import com.flipperdevices.bsbwearable.root.api.model.RootNavigationConfig
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeComponent
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class RootDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val activeTimerScreenDecomposeComponentFactory: ActiveTimerScreenDecomposeComponent.Factory
) : RootDecomposeComponent(),
    ComponentContext by componentContext {
    override val stack: Value<ChildStack<RootNavigationConfig, DecomposeComponent>> = childStack(
        source = navigation,
        serializer = RootNavigationConfig.serializer(),
        initialStack = {
            listOf(RootNavigationConfig.HelloWord)
        },
        handleBackButton = true,
        childFactory = ::child,
    )

    private class HelloWorldDecomposeComponent(
        componentContext: ComponentContext
    ) : ScreenDecomposeComponent(componentContext) {
        @Composable
        override fun Render(modifier: Modifier) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Hello World",
                    color = Color.Red
                )
            }
        }
    }

    private fun child(
        config: RootNavigationConfig,
        componentContext: ComponentContext
    ): DecomposeComponent = when (config) {
        RootNavigationConfig.HelloWord -> HelloWorldDecomposeComponent(componentContext)
        RootNavigationConfig.Active -> activeTimerScreenDecomposeComponentFactory.invoke(
            componentContext = componentContext
        )
    }

    @Composable
    override fun Render(modifier: Modifier) {
        SwipeToDismissBox(
            stack = stack,
            modifier = Modifier.fillMaxSize(),
            onDismiss = {},
            content = { child -> child.instance.Render(Modifier) }
        )
    }

    @Inject
    @ContributesBinding(AppGraph::class, RootDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
        ) -> RootDecomposeComponentImpl
    ) : RootDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ) = factory(componentContext)
    }
}
