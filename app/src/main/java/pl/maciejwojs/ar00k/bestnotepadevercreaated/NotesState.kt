/**
 * @file NotesState.kt
 * @brief Plik definiujący stan notatek w aplikacji.
 *
 * Plik zawiera definicję klasy `NotesState`, która reprezentuje stan notatek, notatek w koszu oraz status uwierzytelnienia użytkownika.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated

import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note

/**
 * Klasa reprezentująca stan notatek w aplikacji.
 *
 * @property notes Lista notatek.
 * @property trashNotes Lista notatek oznaczonych jako usunięte.
 * @property authenticated Flaga określająca, czy użytkownik jest uwierzytelniony.
 */
data class NotesState(
    val notes: List<Note> = emptyList(),
    val trashNotes: List<Note> = emptyList(),
    val authenticated: Boolean = false,
    //    val title: String = "",
    //    val content: String = "",
    //    val modificationTime: String = "",
    //    val creationTime: String = "",
)