package pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag

data class TagsWithNotes(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "noteID",
        entityColumn = "tagID",
        associateBy = Junction(NotesTagsCrossRef::class)
    )
    val tags: List<Tag> // Changed `notes` to `tags`
)