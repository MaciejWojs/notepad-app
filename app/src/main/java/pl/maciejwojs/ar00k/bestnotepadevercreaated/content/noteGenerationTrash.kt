/**
 * Plik zawiera funkcje generujące notatki w koszu.
 *
 * @file noteGenerationTrash.kt
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.content

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pl.maciejwojs.ar00k.bestnotepadevercreaated.CreateNoteTitle
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.utils.getFirst25Words
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.settings.roundness

/**
 * Komponent Compose generujący widok notatki w koszu.
 *
 * @param modifier Modyfikator do dostosowania wyglądu komponentu.
 * @param note Obiekt notatki, który ma być wyświetlony.
 * @param onDelete Funkcja wywoływana po kliknięciu przycisku usunięcia.
 * @param onRestore Funkcja wywoływana po kliknięciu przycisku przywrócenia.
 * @param weight Opcjonalna waga czcionki dla tytułu notatki.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateNoteTrash(
    modifier: Modifier = Modifier,
    note: Note,
    onDelete: () -> Unit,
    onRestore: () -> Unit,
    weight: FontWeight? = null,
) {
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Card(
        modifier =
            Modifier
                .shadow(elevation = 20.dp, spotColor = MaterialTheme.colorScheme.onSurface)
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = { showBottomSheet = true })
                },
    ) {
        Box(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(roundness.dp))
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(roundness.dp),
                    )
                    .padding(7.dp)
                    .scale(0.9f)
                    .height(250.dp)
                    .fillMaxWidth(0.8f)
                    .then(modifier),
        ) {
            Column {
                CreateNoteTitle(
                    noteTitle = note.title,
                    weight = weight,
                    modifier = Modifier.padding(bottom = 20.dp),
                )
                Text(
                    text =
                        if (note.content.split(" ").size > 25) {
                            "${getFirst25Words(note.content)} ..."
                        } else {
                            note.content
                        },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Column(
                modifier = Modifier.align(Alignment.BottomEnd),
            ) {
                Text(
                    text = note.modificationTime,
                    modifier = Modifier,
                )
                Text(
                    text = note.creationTime,
                    modifier = Modifier,
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(50.dp))
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = onRestore) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Restore note")
                    Text(text = "Restore note")
                }
                Button(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete note")
                    Text(text = "Delete note")
                }
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) showBottomSheet = false
                        }
                    },
                ) {
                    Text("Hide")
                }
            }
        }
    }
}
