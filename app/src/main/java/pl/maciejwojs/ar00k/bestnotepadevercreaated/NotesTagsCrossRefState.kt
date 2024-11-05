package pl.maciejwojs.ar00k.bestnotepadevercreaated

import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.NotesWithTags
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.relations.TagsWithNotes

data class NotesTagsCrossRefState(
    val notesWithTag: List<NotesWithTags> = emptyList(),
    val tagsWithNote: List<TagsWithNotes> = emptyList()
)
