package pl.maciejwojs.ar00k.bestnotepadevercreaated.content.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

/**
 * Kompozycyjna funkcja wyświetlająca tytuł notatki.
 *
 * Ta funkcja wyświetla tekst tytułu notatki z określoną grubością czcionki.
 *
 * @param noteTitle Tytuł notatki do wyświetlenia.
 * @param modifier [Modifier] zastosowany do kompozycyjnego układu. Domyślnie jest to pusty modyfikator.
 * @param weight [FontWeight] zastosowany do stylu czcionki tekstu tytułu notatki. Domyślnie jest to FontWeight(900).
 */
@Composable
fun GenerateNoteTitle(
    noteTitle: String,
    modifier: Modifier = Modifier,
    weight: FontWeight? = FontWeight(weight = 900),
) {
    Text(
        text = noteTitle,
        modifier = modifier,
        fontWeight = weight,
    )
}
