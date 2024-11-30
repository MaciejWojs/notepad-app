package pl.maciejwojs.ar00k.bestnotepadevercreaated.content.utils

fun getFirst25Words(text: String): String {
    return text.split(" ") // Split the text by spaces to get words
        .take(25) // Take the first 100 words
        .joinToString(" ") // Join the words back into a single string with spaces
}
