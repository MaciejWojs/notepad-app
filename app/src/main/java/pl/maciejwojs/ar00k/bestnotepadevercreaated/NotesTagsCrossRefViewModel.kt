/**
 * @file NotesTagsCrossRefViewModel.kt
 * @brief Plik definiujący ViewModel dla relacji między notatkami a tagami.
 *
 * Plik zawiera definicję klasy `NotesTagsCrossRefViewModel`, która zarządza stanem relacji między notatkami a tagami w aplikacji.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Klasa ViewModel zarządzająca stanem relacji między notatkami a tagami.
 *
 * @property dao Obiekt DAO do zarządzania operacjami na bazie danych.
 */
data class NotesTagsCrossRefViewModel(
    private val dao: NotesDao,
) : ViewModel() {
    private val _state = MutableStateFlow(NotesTagsCrossRefState())
    val state: StateFlow<NotesTagsCrossRefState> get() = _state

    /**
     * Funkcja ładująca notatki przypisane do danego tagu.
     *
     * @param tagId Identyfikator tagu.
     */
    fun loadNotesByTag(tagId: Long) {
        viewModelScope.launch {
            val notes = dao.getNotesWithTags(tagId)
            _state.value = NotesTagsCrossRefState(notesWithTag = notes)
        }
    }

    /**
     * Funkcja ładująca tagi przypisane do danej notatki.
     *
     * @param noteID Identyfikator notatki.
     */
    fun loadTagsByNote(noteID: Long) {
        viewModelScope.launch {
            val tags = dao.getTagsWithNotes(noteID)
            _state.value = NotesTagsCrossRefState(tagsWithNote = tags)
//            Log.i("Notes", "${_state.value.tagsWithNote.map{it.tags.flatMap { it.name }")
        }
    }
}