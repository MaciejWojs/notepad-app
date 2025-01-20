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
 * Klasa Note reprezentuje notatkę w bazie danych.
 *
 * @property title Tytuł notatki.
 * @property content Treść notatki.
 * @property isPrivate Określa, czy notatka jest prywatna.
 * @property isDeleted Określa, czy notatka została usunięta.
 * @property imageFile Opcjonalny plik graficzny w notatce.
 * @property audioFile Opcjonalny plik dźwiękowy w notatce.
 * @property creationTime Czas utworzenia notatki.
 * @property modificationTime Czas ostatniej modyfikacji notatki.
 * @property noteID Unikalny identyfikator notatki.
 */
@Parcelize
@Entity(tableName = "notes")
data class Note(
    val title: String,
    val content: String,
    val isPrivate: Boolean = false,
    val isDeleted: Boolean = false,
    val imageFile: String? = null,
    val audioFile: String? = null,
    val creationTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")),
    val modificationTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")),
    @PrimaryKey(autoGenerate = true) val noteID: Long = 0,
) : Parcelable
// TODO zrobić pole na zablokowanie notatki i dostosować resztę kodu
// TODO zmienić pola na Timestamp i dostosować resztę kodu
// TODO dodać pole na usuniętą notatke(soft delete,tag jako kosz) i dostosować resztę kodu
