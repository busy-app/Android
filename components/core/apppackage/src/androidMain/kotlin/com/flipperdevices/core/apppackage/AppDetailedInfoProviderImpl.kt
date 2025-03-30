package com.flipperdevices.core.apppackage

import android.content.Context
import android.content.pm.PackageManager
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppDetailedInfoProvider::class)
class AppDetailedInfoProviderImpl(
    private val context: Context
) : AppDetailedInfoProvider {
    override suspend fun provideAppInfo(packageId: String): AppDetailedInfo? {
        val applicationInfo = runCatching {
            context.packageManager.getApplicationInfo(
                packageId,
                PackageManager.GET_META_DATA
            )
        }.getOrNull() ?: return null

        return AppDetailedInfo(
            packageId = packageId,
            categoryId = applicationInfo.category
        )
    }
}
