package com.flipperdevices.bsb.profile.main.api

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.router.stack.replaceAll
import com.flipperdevices.bsb.auth.main.api.AuthDecomposeComponent
import com.flipperdevices.bsb.cloud.model.BSBUser
import com.flipperdevices.bsb.deeplink.model.Deeplink
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.api.get
import com.flipperdevices.bsb.preference.api.set
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.bsb.profile.main.model.ProfileNavigationConfig
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeComponent
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.decompose.findChildByConfig
import com.flipperdevices.ui.decompose.popOr
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class ProfileDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onBackParameter: DecomposeOnBackParameter,
    @Assisted private val deeplink: Deeplink.Root.Auth?,
    private val preferenceApi: PreferenceApi,
    private val authDecomposeComponentFactory: AuthDecomposeComponent.Factory,
    private val profileScreenFactory:
    (ComponentContext, BSBUser, onLogout: () -> Unit) -> ProfileMainDecomposeComponentImpl
) : ProfileDecomposeComponent<ProfileNavigationConfig>(),
    ComponentContext by componentContext {
    override val stack = childStack(
        source = navigation,
        serializer = ProfileNavigationConfig.serializer(),
        initialConfiguration = if (deeplink != null) {
            ProfileNavigationConfig.Login(deeplink)
        } else {
            getCurrentConfig()
        },
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun getCurrentConfig(): ProfileNavigationConfig {
        val userData = preferenceApi.get<BSBUser?>(SettingsEnum.USER_DATA, null)
        return if (userData == null) {
            ProfileNavigationConfig.Login(null)
        } else {
            ProfileNavigationConfig.Main(userData)
        }
    }

    private fun child(
        config: ProfileNavigationConfig,
        componentContext: ComponentContext
    ): DecomposeComponent {
        return when (config) {
            is ProfileNavigationConfig.Login -> authDecomposeComponentFactory(
                componentContext,
                onBackParameter = { navigation.popOr(onBackParameter::invoke) },
                deeplink = config.deeplink,
                onComplete = { navigation.replaceAll(getCurrentConfig()) }
            )

            is ProfileNavigationConfig.Main -> profileScreenFactory(
                componentContext,
                config.user,
                {
                    preferenceApi.set<BSBUser?>(SettingsEnum.USER_DATA, null)
                    navigation.replaceAll(getCurrentConfig())
                }
            )
        }
    }

    override fun handleDeeplink(deeplink: Deeplink.Root.Auth) {
        val child = stack.findChildByConfig(ProfileNavigationConfig.Login::class) ?: return
        val component = child.instance
        if (component != null && component is AuthDecomposeComponent<*>) {
            component.handleDeeplink(deeplink)
            navigation.pushToFront(child.configuration)
        } else {
            navigation.pushToFront(ProfileNavigationConfig.Login(deeplink))
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, ProfileDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter,
            deeplink: Deeplink.Root.Auth?
        ) -> ProfileDecomposeComponentImpl
    ) : ProfileDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter,
            deeplink: Deeplink.Root.Auth?
        ) = factory(componentContext, onBackParameter, deeplink)
    }
}
