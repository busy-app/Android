package com.flipperdevices.bsb.timer.tag.dao

import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
class TagsDao {
    private fun getInitialTags(): List<String> {
        return listOf(
            "Focus",
            "Study",
            "Work",
            "Fitness",
            "Read"
        )
    }

    private val tagsStateFlow = MutableStateFlow(getInitialTags())
    fun tagsFlow(): Flow<List<String>> = tagsStateFlow.asStateFlow()
    fun addTag(tag: String) {
        tagsStateFlow.update { tags -> tags.plus(tag).distinct() }
    }
}
