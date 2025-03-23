package com.flipperdevices.bsb.dao.model

sealed interface BlockedAppEntity {
    data class Category(val categoryId: Int): BlockedAppEntity

    data class App(val packageId: String): BlockedAppEntity
}