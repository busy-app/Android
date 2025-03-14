package com.flipperdevices.core.di

import kotlin.reflect.KProperty

operator fun <T> KIProvider<T>.provideDelegate(
    receiver: Any?,
    property: KProperty<*>
): Lazy<T> = lazy { invoke() }
