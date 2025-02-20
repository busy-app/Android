package com.flipperdevices.bsb.appblocker.filter.model.list

import android.content.pm.ApplicationInfo
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_category_accessibility
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_category_audio
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_category_game
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_category_image
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_category_maps
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_category_news
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_category_productivity
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_category_social
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_category_undefined
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_category_video
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_accessibility
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_games
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_image
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_music
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_news
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_other
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_productivity
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_social
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_travel
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_video
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class AppCategory(
    val title: StringResource,
    val icon: DrawableResource,
    val id: Int
) {
    CATEGORY_UNDEFINED(
        title = Res.string.appblocker_filter_category_undefined,
        icon = Res.drawable.ic_app_type_other,
        id = ApplicationInfo.CATEGORY_UNDEFINED
    ),
    CATEGORY_GAME(
        title = Res.string.appblocker_filter_category_game,
        icon = Res.drawable.ic_app_type_games,
        id = ApplicationInfo.CATEGORY_GAME
    ),
    CATEGORY_AUDIO(
        title = Res.string.appblocker_filter_category_audio,
        icon = Res.drawable.ic_app_type_music,
        id = ApplicationInfo.CATEGORY_AUDIO
    ),
    CATEGORY_VIDEO(
        title = Res.string.appblocker_filter_category_video,
        icon = Res.drawable.ic_app_type_video,
        id = ApplicationInfo.CATEGORY_VIDEO

    ),
    CATEGORY_IMAGE(
        title = Res.string.appblocker_filter_category_image,
        icon = Res.drawable.ic_app_type_image,
        id = ApplicationInfo.CATEGORY_IMAGE
    ),
    CATEGORY_SOCIAL(
        title = Res.string.appblocker_filter_category_social,
        icon = Res.drawable.ic_app_type_social,
        id = ApplicationInfo.CATEGORY_SOCIAL
    ),
    CATEGORY_NEWS(
        title = Res.string.appblocker_filter_category_news,
        icon = Res.drawable.ic_app_type_news,
        id = ApplicationInfo.CATEGORY_NEWS
    ),
    CATEGORY_MAPS(
        title = Res.string.appblocker_filter_category_maps,
        icon = Res.drawable.ic_app_type_travel,
        id = ApplicationInfo.CATEGORY_MAPS
    ),
    CATEGORY_PRODUCTIVITY(
        title = Res.string.appblocker_filter_category_productivity,
        icon = Res.drawable.ic_app_type_productivity,
        id = ApplicationInfo.CATEGORY_PRODUCTIVITY
    ),
    CATEGORY_ACCESSIBILITY(
        title = Res.string.appblocker_filter_category_accessibility,
        icon = Res.drawable.ic_app_type_accessibility,
        id = ApplicationInfo.CATEGORY_ACCESSIBILITY
    );

    companion object {
        fun fromCategoryId(categoryId: Int): AppCategory {
            return entries.find { it.id == categoryId }
                ?: CATEGORY_UNDEFINED
        }

        fun isAllCategoryContains(categories: List<Int>): Boolean {
            val categoriesSet = categories.toSet()
            val notContainsCategory = entries.find { categoriesSet.contains(it.id).not() }
            return notContainsCategory == null
        }
    }
}
