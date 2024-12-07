/**
 * @file NotesViewModel.kt
 * @brief Plik definiujący ViewModel do zarządzania stanem notatek w aplikacji.
 *
 * Plik zawiera definicję klasy `NotesViewModel`, która zarządza stanem notatek, notatek w koszu oraz statusem uwierzytelnienia użytkownika w aplikacji.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note

/**
 * Klasa ViewModel zarządzająca stanem notatek w aplikacji.
 *
 * @property dao Obiekt DAO do zarządzania operacjami na bazie danych.
 */
class NotesViewModel(
    private val dao: NotesDao,
) : ViewModel() {
    private val _state = MutableStateFlow(NotesState())
    val state: StateFlow<NotesState> get() = _state

    private val _trashNotes = MutableStateFlow<List<Note>>(emptyList())
    val trashNotes: StateFlow<List<Note>> get() = _trashNotes

    private val _authenticated = MutableStateFlow(false)
    val authenticated: StateFlow<Boolean> get() = _authenticated

    init {
        // Ładowanie notatek przy tworzeniu ViewModel
        viewModelScope.launch {
            dao.getNotes().collect { notes ->
                _state.update { it.copy(notes = notes) }
                Log.i("BAZA", "Liczba notatek przy uruchomieniu: ${notes.size}")
            }
        }
        // Ładowanie notatek w koszu przy tworzeniu ViewModel
        viewModelScope.launch {
            Log.i("BAZA", "Przed wywołaniem getTrashNotes")
            dao.getTrashNotes().collect { trashNotes ->
                Log.i("BAZA", "Collecting trash notes")
                _trashNotes.update { trashNotes }
                Log.i("BAZA", "Liczba notatek w koszu przy uruchomieniu: ${trashNotes.size}")
            }
            Log.i("BAZA", "Po wywołaniu getTrashNotes")
        }
    }

    /**
     * Funkcja obsługująca zdarzenia związane z notatkami.
     *
     * @param event Zdarzenie dotyczące notatki.
     */
    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                    _trashNotes.update { it - event.note }
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
                    Log.d("isPrivate", "Updating note privacy -> ${event.note.isPrivate}")
                    viewModelScope.launch {
                        dao.updateNote(
                            id = event.note.noteID,
                            title = event.note.title,
                            content = event.note.content,
                            isPrivate = event.note.isPrivate,
                        )
                    }
                } else {
                    Log.i("BAZA", "Nie zapisano notatki, bo brak treści lub tytułu")
                }
            }

            is NotesEvent.UpdateNoteTrash -> {
                viewModelScope.launch {
                    val isDeleted: Boolean = event.note.isDeleted
                    dao.updateNoteDeleted(event.note.noteID, !isDeleted)
                    if (!isDeleted) {
                        _trashNotes.update { it + event.note }
                    } else {
                        _trashNotes.update { it - event.note }
                    }
                }
            }

            is NotesEvent.InsertNote -> {
                viewModelScope.launch {
                    Log.d("BAZA", "Inserting note")
                    val insertedNoteID = dao.insertNote(event.note)
                    if (event.map.isNotEmpty()) {
                        event.map.forEach { entry ->
                            if (entry.value) {
                                dao.insertRelationBetweenNoteAndTag(
                                    insertedNoteID,
                                    entry.key.tagID,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Funkcja ustawiająca status uwierzytelnienia użytkownika.
     *
     * @param logged Flaga określająca, czy użytkownik jest zalogowany.
     */
    fun setAuthenticated(logged: Boolean) {
//        _authenticaded.value = logged
        viewModelScope.launch {
            _authenticated.update { logged }
            Log.d("TestPage", "Logged: $logged")
            Log.d("TestPage", "VIEWMODEL: Access authenticated: ${authenticated.value}")
        }
    }
}
