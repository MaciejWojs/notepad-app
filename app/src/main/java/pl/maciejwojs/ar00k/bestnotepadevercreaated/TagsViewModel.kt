package pl.maciejwojs.ar00k.bestnotepadevercreaated

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TagsViewModel(
    private val dao: NotesDao,
) : ViewModel() {
    private val _state = MutableStateFlow(TagsState())
    val state: StateFlow<TagsState> get() = _state

    init {
        viewModelScope.launch {
            dao.getTags().collect { tags ->
                _state.update { it.copy(tags = tags) }
            }
        }
    }

    fun onEvent(event: TagsEvent) {
        when (event) {
            is TagsEvent.DeleteTag -> {
                viewModelScope.launch {
                    dao.deleteTag(event.tag)
                }
            }

            TagsEvent.SaveTags -> TODO()
            is TagsEvent.SetName -> TODO()
        }
    }
}
