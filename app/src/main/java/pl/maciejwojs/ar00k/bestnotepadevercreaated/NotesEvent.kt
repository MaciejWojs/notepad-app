package pl.maciejwojs.ar00k.bestnotepadevercreaated

import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag

sealed interface NotesEvent {
    object SaveNotes : NotesEvent
    data class SetTitle(val title: String) : NotesEvent
    data class SetContent(val content: String) : NotesEvent
    data class setModificationTime(val modificationTime: String): NotesEvent
    data class DeleteNote(val note: Note): NotesEvent
}

sealed interface TagsEvent {
    object SaveTags: TagsEvent
    data class SetName(val name: String) : TagsEvent
    data class DeleteTag(val tag: Tag): TagsEvent

}