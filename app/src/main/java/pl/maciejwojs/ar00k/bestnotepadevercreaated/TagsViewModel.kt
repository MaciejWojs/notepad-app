/**
 * @file TagsViewModel.kt
 * @brief Plik definiujący ViewModel do zarządzania stanem tagów w aplikacji.
 *
 * Plik zawiera definicję klasy `TagsViewModel`, która zarządza stanem tagów oraz operacjami na tagach w aplikacji.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Klasa ViewModel zarządzająca stanem tagów w aplikacji.
 *
 * @property dao Obiekt DAO do zarządzania operacjami na bazie danych.
 */
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

    /**
     * Funkcja obsługująca zdarzenia związane z tagami.
     *
     * @param event Zdarzenie dotyczące tagu.
     */
    fun onEvent(event: TagsEvent) {
        when (event) {
            is TagsEvent.DeleteTag -> {
                viewModelScope.launch {
                    dao.deleteTag(event.tag)
                }
            }

            is TagsEvent.UpdateTag -> {
                if (event.tag.name.isNotEmpty()) {
                    viewModelScope.launch {
                        dao.updateTag(id = event.tag.tagID, name = event.tag.name)
                    }
                } else {
                    Log.i("BAZA", "Nie zaktualizowano tagu, bo brak nazwy")
                }
            }

            is TagsEvent.SaveTag -> {
                if (event.tag.name.isNotEmpty()) {
                    viewModelScope.launch {
                        dao.insertTag(event.tag)
                    }
                } else {
                    Log.i("BAZA", "Nie zapisano nowego tagu, bo brak nazwy")
                }
            }

            is TagsEvent.AddTagToNote -> {
                viewModelScope.launch {
                    if (dao.checkIfRelationBetweenNoteAndTagExist(
                            noteID = event.noteID,
                            tagID = event.tagID,
                        ) == 0
                    ) {
                        dao.insertRelationBetweenNoteAndTag(event.noteID, event.tagID)
                        Log.i("BAZA", "Zapisano relację")
                    } else {
                        Log.i("BAZA", "Nie zapisano relacji, bo już istnieje")
                    }
                }
            }

            is TagsEvent.RemoveTagFromNote -> {
                viewModelScope.launch {
                    if (dao.checkIfRelationBetweenNoteAndTagExist(
                            noteID = event.noteID,
                            tagID = event.tagID,
                        ) == 1
                    ) {
                        dao.deleteRelationBetweenNoteAndTag(event.noteID, event.tagID)
                        Log.i("BAZA", "Usunięto relację")
                    } else {
                        Log.i("BAZA", "Nie usunięto relacji, bo nie istnieje")
                    }
                }
            }
        }
    }
}
