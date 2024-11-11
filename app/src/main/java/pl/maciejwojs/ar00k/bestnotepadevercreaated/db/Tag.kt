/**
 * @file Tag.kt
 * @brief Plik zawiera implementację klasy Tag.
 *
 * Plik ten definiuje encję Tag, która reprezentuje tag w bazie danych.
 * Klasa Tag zawiera nazwę oraz unikalny identyfikator.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class Tag(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val tagID: Long = 0,
)
