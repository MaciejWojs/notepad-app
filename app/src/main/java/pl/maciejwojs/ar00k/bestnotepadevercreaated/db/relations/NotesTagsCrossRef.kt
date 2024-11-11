/**
 * @file NotesTagsCrossRef.kt
 * @brief Plik zawiera implementację relacji między notatkami a tagami.
 *
 * Plik ten definiuje encję NotesTagsCrossRef, która reprezentuje relację
 * między notatkami a tagami w bazie danych.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["noteID", "tagID"], indices = [Index("noteID"), Index("tagID")])
data class NotesTagsCrossRef(
    val noteID: Long,
    val tagID: Long,
)
