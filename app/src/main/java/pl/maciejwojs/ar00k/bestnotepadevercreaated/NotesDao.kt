/**
 * @file
 * @brief Interfejs DAO (Data Access Object) do obsługi operacji związanych z notatkami i tagami w bazie danych.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Settings
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.NotesWithTags
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.TagsWithNotes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Interfejs DAO (Data Access Object) do obsługi operacji związanych z notatkami i tagami w bazie danych.
 *
 * Definiuje metody do zarządzania notatkami, tagami oraz ich wzajemnymi relacjami.
 */
@Dao
interface NotesDao {
    /**
     * Sprawdza, czy relacje między notatkami a tagami zostały już dodane do bazy danych.
     *
     * @return Liczba relacji w tabeli `NotesTagsCrossRef`.
     */
    @Query("SELECT COUNT(*) from NotesTagsCrossRef")
    suspend fun isAddingRelations(): Long

    /**
     * Dodaje przykładowe relacje między notatkami a tagami przy uruchomieniu aplikacji.
     */
    @Query("INSERT INTO NotesTagsCrossRef VALUES (2,1), (1,1), (1,2)")
    suspend fun insertRelation()

    // Kwerendy do Notatek

    /**
     * Wstawia nową notatkę do bazy danych lub zastępuje istniejącą.
     *
     * @param note Obiekt [Note] reprezentujący notatkę do wstawienia.
     */
    @Insert(entity = Note::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    /**
     * Aktualizuje tytuł, treść oraz czas modyfikacji notatki.
     *
     * @param id Identyfikator notatki do zaktualizowania.
     * @param title Nowy tytuł notatki.
     * @param content Nowa treść notatki.
     * @param modificationDate Data modyfikacji, domyślnie aktualny czas.
     */

    @Query("UPDATE notes SET title=:title, content=:content,modificationTime=:modificationDate, isPrivate=:isPrivate WHERE noteID = :id")
    suspend fun updateNote(
        id: Long,
        title: String,
        content: String,
        isPrivate: Boolean,
        modificationDate: String =
            LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")),
    )

    /**
     * Usuwa notatkę z bazy danych.
     *
     * @param note Notatka do usunięcia.
     */
    @Delete
    suspend fun deleteNote(note: Note)

    /**
     * Pobiera listę wszystkich notatek jako przepływ [Flow].
     *
     * @return [Flow] zawierający listę notatek.
     */
    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Note>>

    /**
     * Zlicza liczbę notatek w bazie danych.
     *
     * @return Liczba notatek w bazie danych.
     */
    @Query("SELECT COUNT(*) FROM notes")
    suspend fun getNotesCount(): Long

    /**
     * Pobiera notatkę na podstawie identyfikatora.
     *
     * @param id Identyfikator notatki do pobrania.
     * @return Obiekt [Note] reprezentujący notatkę o podanym identyfikatorze.
     */

    @Transaction
    @Query("SELECT * FROM notes WHERE noteID=:id")
    suspend fun getNoteFromID(id: Long): Note

    @Query("UPDATE notes SET isPrivate = :isPrivate WHERE noteID = :id")
    suspend fun updateNotePrivacy(
        id: Long,
        isPrivate: Boolean,
    )

    @Query("UPDATE notes SET isDeleted = :isDeleted WHERE noteID = :id")
    suspend fun updateNoteDeleted(
        id: Long,
        isDeleted: Boolean,
    )

    @Query("SELECT * FROM notes WHERE isDeleted = 1")
    fun getTrashNotes(): Flow<List<Note>>

    // Kwerendy do Tagów

    /**
     * Wstawia nowy tag do bazy danych lub zastępuje istniejący.
     *
     * @param tag Obiekt [Tag] reprezentujący tag do wstawienia.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    /**
     * Aktualizuje nazwę tagu na podstawie identyfikatora.
     *
     * @param id Identyfikator tagu do zaktualizowania.
     * @param name Nowa nazwa tagu.
     */
    @Query("UPDATE tags SET name=:name WHERE tagID = :id")
    suspend fun updateTag(
        id: Long,
        name: String,
    )

    /**
     * Usuwa tag z bazy danych.
     *
     * @param tag Tag do usunięcia.
     */
    @Delete
    suspend fun deleteTag(tag: Tag)

    /**
     * Pobiera listę wszystkich tagów jako przepływ [Flow].
     *
     * @return [Flow] zawierający listę tagów.
     */
    @Transaction
    @Query("Select * FROM tags")
    fun getTags(): Flow<List<Tag>>

    /**
     * Zlicza liczbę tagów w bazie danych.
     *
     * @return Liczba tagów w bazie danych.
     */
    @Query("SELECT COUNT(*) FROM tags")
    suspend fun getTagsCount(): Long

    /**
     * Pobiera nazwę tagu na podstawie identyfikatora jako przepływ [Flow].
     *
     * @param id Identyfikator tagu do pobrania.
     * @return [Flow] zawierający listę nazw tagów o podanym identyfikatorze.
     */
    @Transaction
    @Query("SELECT name FROM tags WHERE tagID=:id")
    fun getTagFromID(id: Long): Flow<List<String>>

    /**
     * Pobiera listę notatek przypisanych do danego tagu na podstawie identyfikatora tagu.
     *
     * @param tagId Identyfikator tagu, dla którego mają być pobrane notatki.
     * @return Lista [NotesWithTags] zawierająca notatki powiązane z tagcheiem.
     */
    @Transaction
    @Query("SELECT * FROM tags WHERE tagID = :tagId")
    suspend fun getNotesWithTags(tagId: Long): List<NotesWithTags>

    /**
     * Pobiera listę tagów przypisanych do danej notatki na podstawie identyfikatora notatki.
     *
     * @param noteId Identyfikator notatki, dla której mają być pobrane tagi.
     * @return Lista [TagsWithNotes] zawierająca tagi powiązane z notatką.
     */
    @Transaction
    @Query("SELECT * FROM notes WHERE noteID = :noteId")
    suspend fun getTagsWithNotes(noteId: Long): List<TagsWithNotes>

    @Query("INSERT INTO NotesTagsCrossRef VALUES (:noteID, :tagID)")
    suspend fun insertRelationBetweenNoteAndTag(
        noteID: Long,
        tagID: Long,
    )

    @Query("DElETE FROM notestagscrossref WHERE noteID=:noteID AND tagID=:tagID")
    suspend fun deleteRelationBetweenNoteAndTag(
        noteID: Long,
        tagID: Long,
    )

    @Query("SELECT COUNT(*) FROM notestagscrossref WHERE noteID = :noteID AND tagID=:tagID")
    suspend fun checkIfRelationBetweenNoteAndTagExist(
        noteID: Long,
        tagID: Long,
    ): Int

    //    Kwerendy do Ustawień
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: Settings)

    @Query("SELECT * FROM settings")
    fun getSettings(): Flow<List<Settings>>

    @Query("SELECT COUNT(*) FROM settings")
    suspend fun getSettingsCount(): Long

    @Query("UPDATE settings SET isSet = :isSet WHERE settingsID = :id")
    suspend fun updateSettings(
        id: Long,
        isSet: Boolean,
    )
}
