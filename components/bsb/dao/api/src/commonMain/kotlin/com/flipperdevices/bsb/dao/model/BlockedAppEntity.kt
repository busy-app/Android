package com.flipperdevices.bsb.dao.model

sealed interface BlockedAppEntity : Comparable<BlockedAppEntity> {
    val order: Int

    override fun compareTo(other: BlockedAppEntity): Int {
        return order - other.order
    }

    data class Category(
        val categoryId: Int,
        override val order: Int = 0
    ) : BlockedAppEntity

    data class App(
        val packageId: String,
        override val order: Int = 1
    ) : BlockedAppEntity
}
