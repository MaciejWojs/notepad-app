package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.NotesTagsCrossRef

@Database(
    entities = [Note::class, Tag::class, NotesTagsCrossRef::class],
    version = 1,
)
abstract class NotesDatabase : RoomDatabase() {
    abstract val dao: NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

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
