package com.flipperdevices.bsb.appblocker.filter.viewmodel

import com.flipperdevices.bsb.appblocker.filter.model.UIAppCategory
import com.flipperdevices.bsb.appblocker.filter.model.UIAppInformation
import kotlinx.collections.immutable.toPersistentList

object SearchHelper {
    fun filterCategory(appCategory: UIAppCategory, query: String): UIAppCategory? {
        if (query.isBlank()) {
            return appCategory
        }
        val filteredApps = mutableListOf<UIAppInformation>()
        for (app in appCategory.apps) {
            if (app.appName.contains(query, ignoreCase = true)) {
                filteredApps.add(app)
            }
        }
        if (filteredApps.isEmpty()) {
            return null
        }
        return appCategory.copy(
            isHidden = false,
            apps = filteredApps.toPersistentList()
        )
    }
}