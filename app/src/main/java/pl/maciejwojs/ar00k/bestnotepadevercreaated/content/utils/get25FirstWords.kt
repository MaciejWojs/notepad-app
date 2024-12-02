/** Plik zawiera funkcję getFirst25Words, która zwraca pierwsze 25 słów z podanego tekstu.
 *
 * @file get25FirstWords.kt
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.content.utils

/**
 * Funkcja zwraca pierwsze 25 słów z podanego tekstu.
 *
 * @param text Tekst wejściowy, z którego mają być pobrane słowa.
 * @return Pierwsze 25 słów z tekstu, połączone w jeden ciąg znaków.
 */
fun getFirst25Words(text: String): String {
    return text.split(" ") // Podziel tekst na słowa, używając spacji jako separatora
        .take(25) // Weź pierwsze 25 słów
        .joinToString(" ") // Połącz słowa z powrotem w jeden ciąg znaków, używając spacji jako separatora
}
