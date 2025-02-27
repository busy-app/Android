package com.flipperdevices.bsbwearable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.flipperdevices.bsbwearable.composable.WearApp
import com.flipperdevices.bsbwearable.di.WearAppComponent
import com.flipperdevices.bsbwearable.root.api.RootDecomposeComponent
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.ktx.android.toFullString
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info

internal class MainActivity : ComponentActivity(), LogTagProvider {
    override val TAG = "MainActivity"

    private var rootDecomposeComponent: RootDecomposeComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            info {
                "Create new activity with hashcode: ${this.hashCode()} " + "and intent ${intent.toFullString()}"
            }
        } else {
            info {
                "Restore activity from backstack, so return from onCreate method"
            }
        }

        enableEdgeToEdge()

        val appComponent = ComponentHolder.component<WearAppComponent>()

        val rootComponent = appComponent.rootDecomposeComponentFactory.invoke(
            componentContext = defaultComponentContext(),
        ).also { rootDecomposeComponent = it }

        setContent {
            WearApp(
                rootComponent = rootComponent,
                appComponent = appComponent,
            )
        }
    }
}
