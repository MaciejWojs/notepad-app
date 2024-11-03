package pl.maciejwojs.ar00k.bestnotepadevercreaated

import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag

data class TagsState(
    val tags: List<Tag> = emptyList(),
    val name: String = "",
)
