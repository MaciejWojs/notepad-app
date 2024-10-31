package pl.maciejwojs.ar00k.bestnotepadevercreaated.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun GenerateIconButton(
    icon: ImageVector = Icons.Default.Menu,
    message: String = "Menu",
    modifier: Modifier = Modifier,
    transparent: Boolean = false,
    isEnabled: Boolean = true,
    onClick: () -> Unit
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