package com.flipperdevices.bsb.appblocker.filter.model.list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

data class UIAppCategory(
    val categoryEnum: AppCategory,
    val isBlocked: Boolean,
    val apps: ImmutableList<UIAppInformation>,
    val isHidden: Boolean
) {
    fun block(isBlocked: Boolean): UIAppCategory {
        val newList = apps.map { it.copy(isBlocked = isBlocked) }.toPersistentList()
        return copy(
            isBlocked = isBlocked,
            apps = newList
        )
    }

    fun blockApp(packageName: String, isBlocked: Boolean): UIAppCategory {
        val newList = apps.toMutableList()
        newList.replaceAll {
            if (it.packageName == packageName) {
                it.copy(isBlocked = isBlocked)
            } else {
                it
            }
        }
        newList.sortBy { it.appName }
        var isCategoryBlocked = true
        newList.forEach {
            if (it.isBlocked.not()) {
                isCategoryBlocked = false
            }
        }

        return copy(
            isBlocked = isCategoryBlocked,
            apps = newList.toPersistentList()
        )
    }
}
