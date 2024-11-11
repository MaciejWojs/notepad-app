/**
 * @file IconButtonGeneration.kt
 * @brief Plik zawiera implementację generowania przycisków ikon.
 *
 * Plik ten definiuje kompozycje i funkcje związane z wyświetlaniem
 * i zarządzaniem przyciskami ikon w aplikacji.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Generuje przycisk ikony.
 *
 * @param icon Ikona do wyświetlenia. Domyślnie `Icons.Default.Menu`.
 * @param message Opis ikony. Domyślnie "Menu".
 * @param modifier Modyfikator do zastosowania do przycisku. Domyślnie `Modifier`.
 * @param transparent Określa, czy ikona ma być przezroczysta. Domyślnie `false`.
 * @param isEnabled Określa, czy przycisk jest aktywny. Domyślnie `true`.
 * @param onClick Funkcja wywoływana po kliknięciu przycisku.
 */
@Composable
fun GenerateIconButton(
    icon: ImageVector = Icons.Default.Menu,
    message: String = "Menu",
    modifier: Modifier = Modifier,
    transparent: Boolean = false,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    if (transparent) {
        IconButton(onClick, modifier, enabled = isEnabled) {
            Icon(icon, contentDescription = message, tint = Color.Transparent)
        }
    } else {
        IconButton(onClick, modifier, enabled = isEnabled) {
            Icon(icon, contentDescription = message)
        }
    }
}
