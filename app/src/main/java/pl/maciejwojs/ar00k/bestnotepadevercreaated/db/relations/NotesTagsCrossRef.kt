package pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["noteID", "tagID"], indices = [Index("noteID"), Index("tagID")])
data class NotesTagsCrossRef(
    val noteID: Long,
    val tagID: Long,
)
