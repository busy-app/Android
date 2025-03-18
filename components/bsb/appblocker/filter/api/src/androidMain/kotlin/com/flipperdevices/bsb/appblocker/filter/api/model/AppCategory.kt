package com.flipperdevices.bsb.appblocker.filter.api.model

import android.content.pm.ApplicationInfo

enum class AppCategory(val id: Int) {
    CATEGORY_UNDEFINED(id = ApplicationInfo.CATEGORY_UNDEFINED),
    CATEGORY_GAME(id = ApplicationInfo.CATEGORY_GAME),
    CATEGORY_AUDIO(id = ApplicationInfo.CATEGORY_AUDIO),
    CATEGORY_VIDEO(id = ApplicationInfo.CATEGORY_VIDEO),
    CATEGORY_IMAGE(id = ApplicationInfo.CATEGORY_IMAGE),
    CATEGORY_SOCIAL(id = ApplicationInfo.CATEGORY_SOCIAL),
    CATEGORY_NEWS(id = ApplicationInfo.CATEGORY_NEWS),
    CATEGORY_MAPS(id = ApplicationInfo.CATEGORY_MAPS),
    CATEGORY_PRODUCTIVITY(id = ApplicationInfo.CATEGORY_PRODUCTIVITY),
    CATEGORY_ACCESSIBILITY(id = ApplicationInfo.CATEGORY_ACCESSIBILITY);

    companion object
}
