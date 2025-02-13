plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.atomicflu) apply false
    id("flipper.java.version") apply false
    id("flipper.rebuild.composeres") apply false
}

allprojects {
    apply(plugin = "flipper.java.version")
    apply(plugin="flipper.rebuild.composeres")
}