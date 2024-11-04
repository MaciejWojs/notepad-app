package pl.maciejwojs.ar00k.bestnotepadevercreaated

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.NotesWithTags
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.TagsWithNotes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Dao
interface NotesDao {
    //Do wrzucenia relacji przy włączeniu aplikacji
    @Query("SELECT COUNT(*) from NotesTagsCrossRef")
    suspend fun isAddingRelations(): Int

    @Query("INSERT  INTO NotesTagsCrossRef VALUES (2,1), (1,1), (1,2)")
    suspend fun insertRelation()

    //Kwerendy do Notatek
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("UPDATE notes SET title=:title, content=:content,modificationTime=:modificationDate WHERE noteID = :id")
    suspend fun updateNote(
        id: Int,
        title: String,
        content: String,
        modificationDate: String = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"))
    )

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Note>>

    //Zliczanie notatek z bazy danych
    @Query("SELECT COUNT(*) FROM notes")
    suspend fun getNotesCount(): Int

    @Transaction
    @Query("SELECT * FROM notes WHERE noteID=:id")
    suspend fun getNoteFromID(id: Int): Note


    //Kwerendy do TAGOW
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Transaction
    @Query("Select * FROM tags")
    fun getTags(): Flow<List<Tag>>

    //Zliczanie tagów z bazy danych
    @Query("SELECT COUNT(*) FROM tags")
    suspend fun getTagsCount(): Int

    @Transaction
    @Query("SELECT name FROM tags WHERE tagID=:id")
    fun getTagFromID(id: Int): Flow<List<String>>


    @Transaction
    @Query("SELECT * FROM tags WHERE tagID = :tagId")
    suspend fun getNotesWithTags(tagId: Int): List<NotesWithTags>

    @Transaction
    @Query("SELECT * FROM notes WHERE noteID = :noteId")
    suspend fun getTagsWithNotes(noteId: Int): List<TagsWithNotes>


    //Kwerendy Mieszane
}