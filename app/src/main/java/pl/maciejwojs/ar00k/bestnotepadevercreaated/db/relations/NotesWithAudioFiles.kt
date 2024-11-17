package pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.AudioFile

data class NotesWithAudioFiles(
    @Embedded val audioFile: AudioFile,
    @Relation(
        parentColumn = "audioID",
        entityColumn = "noteID",
        associateBy = Junction(AudioFilesNotesCrossRef::class),
    )
    val notes: List<AudioFile>,
)
