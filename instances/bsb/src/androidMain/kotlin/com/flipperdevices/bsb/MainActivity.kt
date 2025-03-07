package com.flipperdevices.bsb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.defaultComponentContext
import com.flipperdevices.bsb.appblocker.screen.api.AppBlockerActivity
import com.flipperdevices.bsb.deeplink.api.DeepLinkParser
import com.flipperdevices.bsb.deeplink.model.Deeplink
import com.flipperdevices.bsb.di.AndroidAppComponent
import com.flipperdevices.bsb.root.api.RootDecomposeComponent
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.ktx.android.toFullString
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity(), LogTagProvider {
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

        val appComponent = ComponentHolder.component<AndroidAppComponent>()

        val rootComponent = appComponent.rootDecomposeComponentFactory(
            defaultComponentContext(),
            initialDeeplink = runBlocking {
                appComponent.deeplinkParser.parseOrLog(
                    this@MainActivity,
                    intent
                )
            }
        ).also { rootDecomposeComponent = it }

        setContent {
            App(rootComponent, appComponent)
        }
        if (savedInstanceState == null) {
            openAppBlockerScreenIfNeed(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        info { "Receive new intent: ${intent?.toFullString()}" }
        if (intent == null) {
            return
        }
        lifecycleScope.launch(FlipperDispatchers.default) {
            val appComponent = ComponentHolder.component<AndroidAppComponent>()
            appComponent.deeplinkParser.parseOrLog(this@MainActivity, intent)?.let {
                withContext(Dispatchers.Main) {
                    rootDecomposeComponent?.handleDeeplink(it)
                }
            }
        }
        openAppBlockerScreenIfNeed(intent)
    }

    private suspend fun DeepLinkParser.parseOrLog(context: Context, intent: Intent): Deeplink? {
        return try {
            fromIntent(context, intent)
        } catch (throwable: Exception) {
            error(throwable) { "Failed parse deeplink" }
            null
        }
    }

    private fun openAppBlockerScreenIfNeed(intent: Intent) {
        val parserApi = ComponentHolder.component<AndroidAppComponent>().applicationInfoParserApi
        if (!parserApi.isApplicationInfoAction(intent.action)) {
            return
        }

        info { "Open app blocker screen" }

        parserApi.parse(intent)
            .mapCatching { applicationInfo ->
                parserApi.getIntent(
                    applicationInfo = applicationInfo,
                    context = this,
                    activity = AppBlockerActivity::class
                )
            }.onSuccess { appBlockedActivityIntent ->
                startActivity(appBlockedActivityIntent)
            }
    }
}
