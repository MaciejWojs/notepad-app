package pl.maciejwojs.ar00k.bestnotepadevercreaated

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.NotesWithTags
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.TagsWithNotes

@Dao
interface NotesDao {
    //Kwerendy do Notatek
    @Upsert
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Note>>

    @Transaction
    @Query("SELECT * FROM notes WHERE noteID=:id")
    fun getNoteFromID(id: Int): Flow<List<Note>>


    //Kwerendy do TAGOW
    @Upsert
    suspend fun insertTag(tag: Tag)

    @Delete
    suspend fun delete(tag: Tag)

    @Transaction
    @Query("Select * FROM tags")
    fun getTags(): Flow<List<Tag>>

    @Transaction
    @Query("SELECT name FROM tags WHERE tagID=:id")
    fun getTagFromID(id: Int): Flow<List<String>>


    //Kwerenda do notatek z tagami
    @Transaction
    @Query("SELECT * FROM Notes WHERE noteID = :noteId")
    fun getNotesWithTags(noteId: Int): List<NotesWithTags>

    @Transaction
    @Query("SELECT * FROM Tags WHERE tagID = :tagId")
    fun getTagsWithNotes(tagId: Int): List<TagsWithNotes>


    //Kwerendy Mieszane
}