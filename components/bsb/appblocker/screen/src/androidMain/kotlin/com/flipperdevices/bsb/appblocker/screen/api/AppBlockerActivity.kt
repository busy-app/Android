package com.flipperdevices.bsb.appblocker.screen.api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import com.flipperdevices.bsb.appblocker.screen.composable.AppBlockerScreenComposable
import com.flipperdevices.bsb.appblocker.screen.di.AppBlockerActivityComponent
import com.flipperdevices.bsb.appblocker.screen.model.toInternal
import com.flipperdevices.bsb.core.theme.BusyBarTheme
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.ktx.android.toFullString
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info

class AppBlockerActivity : ComponentActivity(), LogTagProvider {
    override val TAG = "AppBlockerActivity"

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

        val parserApi = ComponentHolder
            .component<AppBlockerActivityComponent>()
            .applicationInfoParserApi

        val applicationInfo = parserApi.parse(intent)
            .onFailure {
                error(it) { "Fail to parse ${intent.toFullString()}" }
            }.getOrNull()?.toInternal(this)

        if (applicationInfo == null) {
            finish()
            return
        }

        setContent {
            BusyBarTheme(darkMode = true) {
                AppBlockerScreenComposable(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LocalPallet.current.surface.primary)
                        .safeDrawingPadding(),
                    applicationInfo = applicationInfo,
                    onBack = { finish() }
                )
            }
        }
    }
}
