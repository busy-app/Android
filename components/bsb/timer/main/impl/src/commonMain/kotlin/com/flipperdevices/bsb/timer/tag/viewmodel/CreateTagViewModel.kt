package com.flipperdevices.bsb.timer.tag.viewmodel

import com.flipperdevices.bsb.timer.tag.dao.TagsDao
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class CreateTagViewModel(
    private val tagsDao: TagsDao
) : DecomposeViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun onTagChanged(tag: String) {
        _state.update { state -> state.copy(tag = tag) }
    }

    fun onFinish() {
        viewModelScope.launch {
            tagsDao.addTag(state.first().tag)
        }
    }

    data class State(
        val tag: String = "",
    ) {
        val canCreate = tag.length in MIN_TAG_LENGTH..MAX_TAG_LENGTH
    }

    companion object {
        const val MAX_TAG_LENGTH = 12
        const val MIN_TAG_LENGTH = 1
    }
}
