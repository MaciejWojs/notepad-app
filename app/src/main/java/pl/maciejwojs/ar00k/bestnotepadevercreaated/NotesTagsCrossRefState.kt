package pl.maciejwojs.ar00k.bestnotepadevercreaated

import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.NotesWithTags

data class NotesTagsCrossRefState(
    val notesWithTag: List<NotesWithTags> = emptyList(),
)
