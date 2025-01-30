package com.flipperdevices.bsb.timer.tag.viewmodel

import com.flipperdevices.bsb.timer.tag.dao.TagsDao
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject

@Inject
class TagsViewModel(
    private val tagsDao: TagsDao
) : DecomposeViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun select(tag: String) {
        _state.update { state -> state.copy(selectedTag = tag) }
    }

    init {
        tagsDao.tagsFlow()
            .onEach { tags ->
                _state.update { state ->
                    state.copy(
                        tags = tags,
                        selectedTag = tags
                            .firstOrNull()
                            ?.takeIf { state.selectedTag == null }
                    )
                }
            }.launchIn(viewModelScope)
    }

    data class State(
        val tags: List<String> = listOf(),
        val selectedTag: String? = null,
    )
}
