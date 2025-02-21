package com.flipperdevices.bsb.appblocker.filter.model.list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

sealed interface AppBlockerFilterScreenState {
    data object Loading : AppBlockerFilterScreenState

    data class Loaded(
        val categories: ImmutableList<UIAppCategory>
    ) : AppBlockerFilterScreenState {
        fun updateCategory(
            appCategory: AppCategory,
            block: (UIAppCategory) -> UIAppCategory
        ): Loaded {
            val oldCategory = categories.find { it.categoryEnum == appCategory } ?: return this
            val newCategory = block(oldCategory)
            val newCategories = categories.toMutableList()
            newCategories.removeIf { it.categoryEnum == appCategory }
            newCategories.add(newCategory)
            newCategories.sortByDescending { it.categoryEnum.id }

            return Loaded(
                categories = newCategories.toPersistentList()
            )
        }
    }
}
