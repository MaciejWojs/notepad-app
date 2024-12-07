/**
 * @file NotesDatabase.kt
 * @brief Plik definiujący bazę danych notatek.
 *
 * Plik zawiera definicję bazy danych `NotesDatabase`, która zarządza tabelami notatek (`Note`), tagów (`Tag`) oraz relacji między nimi (`NotesTagsCrossRef`).
 * Baza danych jest implementowana przy użyciu biblioteki Room.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Settings
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.AudioFilesNotesCrossRef
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.NotesTagsCrossRef

/**
 * Klasa abstrakcyjna reprezentująca bazę danych notatek.
 *
 * Klasa `NotesDatabase` zarządza tabelami notatek (`Note`), tagów (`Tag`) oraz relacji między nimi (`NotesTagsCrossRef`).
 * Implementacja bazy danych jest realizowana przy użyciu biblioteki Room.
 */
@Database(
    entities = [Note::class, Tag::class, NotesTagsCrossRef::class, AudioFilesNotesCrossRef::class, Settings::class],
    version = 1,
)
abstract class NotesDatabase : RoomDatabase() {
    /**
     * Abstrakcyjna właściwość reprezentująca DAO (Data Access Object) do zarządzania operacjami na bazie danych.
     */
    abstract val dao: NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        /**
         * Funkcja zwracająca instancję bazy danych.
         *
         * @param context Kontekst aplikacji.
         * @return Instancja bazy danych `NotesDatabase`.
         */
        fun getInstance(context: Context): NotesDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "note_db",
                ).build().also { INSTANCE = it }
            }
        }
    }
}
