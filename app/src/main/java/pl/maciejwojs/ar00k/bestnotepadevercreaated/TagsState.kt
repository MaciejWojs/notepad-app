/**
 * @file TagsState.kt
 * @brief Plik definiujący stan tagów w aplikacji.
 *
 * Plik zawiera definicję klasy `TagsState`, która reprezentuje stan tagów w aplikacji.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated

import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag

/**
 * Klasa reprezentująca stan tagów w aplikacji.
 *
 * @property tags Lista tagów.
 */
data class TagsState(
    val tags: List<Tag> = emptyList(),
//    val name: String = "",
)