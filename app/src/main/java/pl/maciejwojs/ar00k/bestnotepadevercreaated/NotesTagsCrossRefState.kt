/**
 * @file NotesTagsCrossRefState.kt
 * @brief Plik definiujący stan relacji między notatkami a tagami.
 *
 * Plik zawiera definicję klasy `NotesTagsCrossRefState`, która reprezentuje stan relacji między notatkami a tagami w aplikacji.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated

import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.NotesWithTags
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.TagsWithNotes

/**
 * Klasa reprezentująca stan relacji między notatkami a tagami.
 *
 * @property notesWithTag Lista notatek z przypisanymi tagami.
 * @property tagsWithNote Lista tagów z przypisanymi notatkami.
 */
data class NotesTagsCrossRefState(
    val notesWithTag: List<NotesWithTags> = emptyList(),
    val tagsWithNote: List<TagsWithNotes> = emptyList(),
)