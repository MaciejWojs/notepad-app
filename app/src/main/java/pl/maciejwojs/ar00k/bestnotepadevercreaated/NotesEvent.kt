package pl.maciejwojs.ar00k.bestnotepadevercreaated

import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag

/**
 * Interfejs reprezentujący zdarzenia związane z notatkami.
 */
sealed interface NotesEvent {
    /**
     * Zdarzenie wstawienia nowej notatki.
     *
     * @param note Obiekt notatki do wstawienia.
     * @param map Mapa tagów przypisanych do notatki.
     */
    data class InsertNote(val note: Note, val map: Map<Tag, Boolean> = emptyMap()) : NotesEvent

    /**
     * Zdarzenie usunięcia notatki.
     *
     * @param note Obiekt notatki do usunięcia.
     */
    data class DeleteNote(val note: Note) : NotesEvent

    /**
     * Zdarzenie zapisania notatki.
     *
     * @param note Obiekt notatki do zapisania.
     */
    data class SaveNote(val note: Note) : NotesEvent

    /**
     * Zdarzenie aktualizacji notatki.
     *
     * @param note Obiekt notatki do aktualizacji.
     */
    data class UpdateNote(val note: Note) : NotesEvent

    /**
     * Zdarzenie przeniesienia notatki do kosza.
     *
     * @param note Obiekt notatki do przeniesienia do kosza.
     */
    data class UpdateNoteTrash(val note: Note) : NotesEvent
}

/**
 * Interfejs reprezentujący zdarzenia związane z tagami.
 */
sealed interface TagsEvent {
    /**
     * Zdarzenie zapisania tagu.
     *
     * @param tag Obiekt tagu do zapisania.
     */
    data class SaveTag(val tag: Tag) : TagsEvent

    /**
     * Zdarzenie aktualizacji tagu.
     *
     * @param tag Obiekt tagu do aktualizacji.
     */
    data class UpdateTag(val tag: Tag) : TagsEvent

    /**
     * Zdarzenie usunięcia tagu.
     *
     * @param tag Obiekt tagu do usunięcia.
     */
    data class DeleteTag(val tag: Tag) : TagsEvent

    /**
     * Zdarzenie dodania tagu do notatki.
     *
     * @param noteID Identyfikator notatki.
     * @param tagID Identyfikator tagu.
     */
    data class AddTagToNote(val noteID: Long, val tagID: Long) : TagsEvent

    /**
     * Zdarzenie usunięcia tagu z notatki.
     *
     * @param noteID Identyfikator notatki.
     * @param tagID Identyfikator tagu.
     */
    data class RemoveTagFromNote(val noteID: Long, val tagID: Long) : TagsEvent
}

/**
 * Interfejs reprezentujący zdarzenia związane z ustawieniami.
 */
sealed interface SettingsEvent {
    /**
     * Zdarzenie załadowania ustawień.
     */
    object LoadSettings : SettingsEvent

    /**
     * Zdarzenie aktualizacji ustawienia.
     *
     * @param key Klucz ustawienia.
     * @param value Wartość ustawienia.
     */
    data class UpdateSetting(val key: String, val value: Any) : SettingsEvent

    /**
     * Zdarzenie resetowania ustawień.
     */
    object ResetSettings : SettingsEvent
}