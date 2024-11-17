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

            is NotesEvent.SaveNote -> {
                if (event.note.content.isNotEmpty() && event.note.title.isNotEmpty()) {
                    viewModelScope.launch {
                        dao.insertNote(event.note)
                    }
                } else {
                    Log.i("BAZA", "Nie zapisano nowej notatki, bo brak treści lub tytułu")
                }
            }

            is NotesEvent.UpdateNote -> {
                if (event.note.content.isNotEmpty() && event.note.title.isNotEmpty()) {
                    viewModelScope.launch {
                        dao.updateNote(
                            id = event.note.noteID,
                            title = event.note.title,
                            content = event.note.content,
                        )
                    }
                } else {
                    Log.i("BAZA", "Nie zapisano notatki, bo brak treści lub tytułu")
                }
            }
        }
    }
}
