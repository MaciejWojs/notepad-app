package pl.maciejwojs.ar00k.bestnotepadevercreaated

import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag

sealed interface NotesEvent {
//    object SaveNotes : NotesEvent

//    data class SetTitle(val title: String) : NotesEvent
//
//    data class SetContent(val content: String) : NotesEvent
//
//    data class setModificationTime(val modificationTime: String) : NotesEvent

    data class InsertNote(val note: Note, val map: Map<Tag, Boolean> = emptyMap()) : NotesEvent

    data class DeleteNote(val note: Note) : NotesEvent

    data class SaveNote(val note: Note) : NotesEvent

    data class UpdateNote(val note: Note) : NotesEvent

    data class UpdateNoteTrash(val note: Note) : NotesEvent
}

sealed interface TagsEvent {
    data class SaveTag(val tag: Tag) : TagsEvent

    data class UpdateTag(val tag: Tag) : TagsEvent

    data class DeleteTag(val tag: Tag) : TagsEvent

    data class AddTagToNote(val noteID: Long, val tagID: Long) : TagsEvent

    data class RemoveTagFromNote(val noteID: Long, val tagID: Long) : TagsEvent
}

sealed interface SettingsEvent {
    object LoadSettings : SettingsEvent

    data class UpdateSetting(val key: String, val value: Any) : SettingsEvent

    object ResetSettings : SettingsEvent
}
