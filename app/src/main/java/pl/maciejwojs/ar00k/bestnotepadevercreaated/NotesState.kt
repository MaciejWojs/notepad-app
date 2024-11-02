package pl.maciejwojs.ar00k.bestnotepadevercreaated

import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note

data class NotesState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val content: String = "",
    val modificationTime: String = "",
    val creationTime: String = "",
)
