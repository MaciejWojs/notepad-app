package pl.maciejwojs.ar00k.bestnotepadevercreaated

sealed interface NotesEvent {
    object SaveNotes : NotesEvent
    data class SetTitle(val title: String) : NotesEvent
    data class SetContent(val content: String) : NotesEvent
    data class setModificationTime(val modificationTime: String): NotesEvent
}