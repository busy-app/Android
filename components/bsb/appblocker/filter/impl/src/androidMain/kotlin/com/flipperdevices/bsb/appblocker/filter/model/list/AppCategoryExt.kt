package com.flipperdevices.bsb.appblocker.filter.model.list

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
import com.flipperdevices.bsb.appblocker.filter.api.model.AppCategory
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

val AppCategory.title: StringResource
    get() = when (this) {
        AppCategory.CATEGORY_UNDEFINED -> Res.string.appblocker_filter_category_undefined
        AppCategory.CATEGORY_GAME -> Res.string.appblocker_filter_category_game
        AppCategory.CATEGORY_AUDIO -> Res.string.appblocker_filter_category_audio
        AppCategory.CATEGORY_VIDEO -> Res.string.appblocker_filter_category_video
        AppCategory.CATEGORY_IMAGE -> Res.string.appblocker_filter_category_image
        AppCategory.CATEGORY_SOCIAL -> Res.string.appblocker_filter_category_social
        AppCategory.CATEGORY_NEWS -> Res.string.appblocker_filter_category_news
        AppCategory.CATEGORY_MAPS -> Res.string.appblocker_filter_category_maps
        AppCategory.CATEGORY_PRODUCTIVITY -> Res.string.appblocker_filter_category_productivity
        AppCategory.CATEGORY_ACCESSIBILITY -> Res.string.appblocker_filter_category_accessibility
    }

val AppCategory.icon: DrawableResource
    get() = when (this) {
        AppCategory.CATEGORY_UNDEFINED -> Res.drawable.ic_app_type_other
        AppCategory.CATEGORY_GAME -> Res.drawable.ic_app_type_games
        AppCategory.CATEGORY_AUDIO -> Res.drawable.ic_app_type_music
        AppCategory.CATEGORY_VIDEO -> Res.drawable.ic_app_type_video
        AppCategory.CATEGORY_IMAGE -> Res.drawable.ic_app_type_image
        AppCategory.CATEGORY_SOCIAL -> Res.drawable.ic_app_type_social
        AppCategory.CATEGORY_NEWS -> Res.drawable.ic_app_type_news
        AppCategory.CATEGORY_MAPS -> Res.drawable.ic_app_type_travel
        AppCategory.CATEGORY_PRODUCTIVITY -> Res.drawable.ic_app_type_productivity
        AppCategory.CATEGORY_ACCESSIBILITY -> Res.drawable.ic_app_type_accessibility
    }

fun AppCategory.Companion.fromCategoryId(categoryId: Int): AppCategory {
    return AppCategory.entries.find { it.id == categoryId }
        ?: AppCategory.CATEGORY_UNDEFINED
}
