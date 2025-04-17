package com.flipperdevices.bsb.timer.cards.api

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeComponent
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@Suppress("LongParameterList")
class CardsDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val pagerDecomposeComponentFactory: (
        componentContext: ComponentContext,
        DecomposeOnBackParameter,
        (CardRenameItem) -> Unit,
        () -> Unit,
    ) -> PagerDecomposeComponentImpl,
    private val renameDecomposeComponentFactory: (
        componentContext: ComponentContext,
        DecomposeOnBackParameter,
        CardRenameItem,
    ) -> RenameDecomposeComponentImpl
) : CardsDecomposeComponent(),
    ComponentContext by componentContext,
    StatusBarIconStyleProvider by iconStyleProvider {

    override val stack: Value<ChildStack<CardsNavigationConfig, DecomposeComponent>> = childStack(
        source = navigation,
        serializer = CardsNavigationConfig.serializer(),
        initialStack = {
            listOf(CardsNavigationConfig.Pager)
        },
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: CardsNavigationConfig,
        componentContext: ComponentContext
    ): DecomposeComponent = when (config) {
        CardsNavigationConfig.Pager -> pagerDecomposeComponentFactory.invoke(
            componentContext,
            {},
            {
                navigation.push(CardsNavigationConfig.Rename(it))
            },
            {
                navigation.push(CardsNavigationConfig.Settings)
            }
        )

        is CardsNavigationConfig.Rename -> renameDecomposeComponentFactory.invoke(
            componentContext,
            {
                navigation.pop()
            },
            config.cardRenameItem
        )

        CardsNavigationConfig.Settings -> TODO()
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun Render(modifier: Modifier) {
        val childStack by stack.subscribeAsState()
        SharedTransitionLayout {
            com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack(
                modifier = modifier,
                stack = childStack,
                animation = stackAnimation(fade())
            ) { child ->
                CompositionLocalProvider(
                    LocalAnimatedVisibilityScope provides this,
                    LocalSharedTransitionScope provides this@SharedTransitionLayout
                ) {
                    child.instance.Render(Modifier)
                }
            }
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, CardsDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> CardsDecomposeComponentImpl
    ) : CardsDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}

val LocalAnimatedVisibilityScope = staticCompositionLocalOf<AnimatedVisibilityScope> {
    error("AnimatedVisibilityScope is not defined")
}


@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> {
    error("SharedTransitionScope is not defined")
}
