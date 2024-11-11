/**
 * @file NotesWithTags.kt
 * @brief Plik zawiera implementację relacji między notatkami a tagami.
 *
 * Plik ten definiuje klasę NotesWithTags, która reprezentuje relację
 * między notatkami a tagami w bazie danych.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag

data class NotesWithTags(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "tagID",
        entityColumn = "noteID",
        associateBy = Junction(NotesTagsCrossRef::class),
    )
    val notes: List<Note>,
)
