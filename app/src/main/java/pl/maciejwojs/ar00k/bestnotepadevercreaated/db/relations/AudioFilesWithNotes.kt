package pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.AudioFile
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note

/**
 * Klasa reprezentująca relację między notatkami a plikami audio.
 *
 * @property note Notatka, do której przypisane są pliki audio.
 * @property audioFiles Lista plików audio powiązanych z notatką.
 */
data class AudioFilesWithNotes(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "noteID",
        entityColumn = "audioID",
        associateBy = Junction(AudioFilesNotesCrossRef::class),
    )
    val audioFiles: List<AudioFile>,
)
