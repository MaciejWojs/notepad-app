package pl.maciejwojs.ar00k.bestnotepadevercreaated

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class NotesTagsCrossRefViewModel(
    private val dao: NotesDao
) : ViewModel() {
    private val _state = MutableStateFlow(NotesTagsCrossRefState())
    val state: StateFlow<NotesTagsCrossRefState> get() = _state

    fun loadNotesByTag(tagId: Int) {
        viewModelScope.launch {
            val notes = dao.getNotesWithTags(tagId)
            _state.value = NotesTagsCrossRefState(notesWithTag = notes)
        }
    }

    fun loadTagsByNote(noteID:Int) {
        viewModelScope.launch {
            val tags = dao.getTagsWithNotes(noteID)
            _state.value = NotesTagsCrossRefState(tagsWithNote = tags)
//            Log.i("Notes", "${_state.value.tagsWithNote.map{it.tags.flatMap { it.name }")
        }
    }
}

