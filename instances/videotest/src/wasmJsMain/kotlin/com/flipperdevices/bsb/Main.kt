package com.flipperdevices.bsb

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.flipperdevices.bsb.di.WasmJSAppComponent
import com.flipperdevices.bsb.di.create
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.russhwolf.settings.StorageSettings
import com.russhwolf.settings.observable.makeObservable
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.w3c.dom.Document
import org.w3c.dom.get
import org.w3c.dom.set

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()
    val stateKeeper = StateKeeperDispatcher(
        savedState = localStorage[KEY_SAVED_STATE]?.decodeSerializableContainer()
    )
    val settings = StorageSettings().makeObservable()
    val applicationScope = CoroutineScope(
        SupervisorJob() + FlipperDispatchers.default
    )
    val appComponent = WasmJSAppComponent::class.create(
        settings,
        applicationScope
    )
    val root = appComponent.rootDecomposeComponentFactory(
        DefaultComponentContext(lifecycle = lifecycle),
        initialDeeplink = null
    )
    lifecycle.attachToDocument()

    window.onbeforeunload = {
        localStorage[KEY_SAVED_STATE] = stateKeeper.save().encodeToString()
        null
    }
    ComposeViewport(document.body!!) {
        App(root, appComponent)
    }
}

private const val KEY_SAVED_STATE = "saved_state"

private fun LifecycleRegistry.attachToDocument() {
    fun onVisibilityChanged() {
        if (visibilityState(document) == "visible") {
            resume()
        } else {
            stop()
        }
    }

    onVisibilityChanged()

    document.addEventListener(type = "visibilitychange", callback = { onVisibilityChanged() })
}

// Workaround for Document#visibilityState not available in Wasm
@JsFun("(document) => document.visibilityState")
private external fun visibilityState(document: Document): String
