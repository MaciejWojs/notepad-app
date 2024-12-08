/**
 * @file Note.kt
 * @brief Plik zawiera implementację klasy Note.
 *
 * Plik ten definiuje encję Note, która reprezentuje notatkę w bazie danych.
 * Klasa Note zawiera tytuł, treść, czas modyfikacji, czas utworzenia oraz unikalny identyfikator.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Encja reprezentująca notatkę w bazie danych.
 *
 * @property title Tytuł notatki.
 * @property content Treść notatki.
 * @property isPrivate Flaga określająca czy notatka jest prywatna.
 * @property isDeleted Flaga określająca czy notatka jest usunięta.
 * @property creationTime Czas utworzenia notatki.
 * @property modificationTime Czas ostatniej modyfikacji notatki.
 * @property noteID Unikalny identyfikator notatki, generowany automatycznie.
 */
@Parcelize
@Entity(tableName = "notes")
data class Note(
    val title: String,
    val content: String,
    val isPrivate: Boolean = false,
    val isDeleted: Boolean = false,
    val imageFile: ByteArray? = null,
    val audioFile: ByteArray? = null,
    val creationTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")),
    val modificationTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")),
    @PrimaryKey(autoGenerate = true) val noteID: Long = 0,
) : Parcelable
// TODO zrobić pole na zablokowanie notatki i dostosować resztę kodu
// TODO zmienić pola na Timestamp i dostosować resztę kodu
// TODO dodać pole na usuniętą notatke(soft delete,tag jako kosz) i dostosować resztę kodu
