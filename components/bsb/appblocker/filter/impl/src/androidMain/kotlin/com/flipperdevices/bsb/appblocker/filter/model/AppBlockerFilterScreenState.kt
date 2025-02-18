package com.flipperdevices.bsb.appblocker.filter.model

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
            val category = categories.find { it.categoryEnum == appCategory } ?: return this
            val newCategory = block(category)
            val newCategories = categories.toMutableList()
            newCategories.removeIf { it.categoryEnum == appCategory }
            newCategories.add(newCategory)
            newCategories.sortBy { it.categoryEnum.id }

            return AppBlockerFilterScreenState.Loaded(
                categories = newCategories.toPersistentList()
            )
        }
    }
}
