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
    version = 1
)

abstract class ContactDatabase : RoomDatabase() {
    abstract val dao: NotesDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        fun getInstance(context: Context): ContactDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "note_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}