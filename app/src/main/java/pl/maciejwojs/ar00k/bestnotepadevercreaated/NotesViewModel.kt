package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    private val dao: NotesDao,
) : ViewModel() {
    private val _state = MutableStateFlow(NotesState())
    val state: StateFlow<NotesState> get() = _state

    init {
        // Load notes initially when the ViewModel is created
        viewModelScope.launch {
            dao.getNotes().collect { notes ->
                _state.update { it.copy(notes = notes) }
                Log.i("BAZA", "Liczba notatek przy uruchomieniu: ${notes.size}")
            }
        }
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }

            NotesEvent.SaveNotes -> TODO()
            is NotesEvent.SetContent -> {
                _state.update {
                    it.copy(
                        content = event.content,
                    )
                }
            }

            is NotesEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title,
                    )
                }
            }

            is NotesEvent.setModificationTime -> TODO()
        }
    }
}
